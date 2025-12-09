import { Client } from '@stomp/stompjs'
import React, { useEffect, useRef, useState } from 'react'
import { useSelector } from 'react-redux'
import { useParams } from 'react-router'
import SockJS from 'sockjs-client'
import jwtAxios from '../../../apis/util/jwtUtil'

const MyCrewChatContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken)
  const senderId = useSelector((state) => state.loginSlice.id)
  const senderNickName = useSelector((state) => state.loginSlice.nickName)
  const {crewId} = useParams()
  const [crewName, setCrewName] = useState("")

  const [isEntered, setIsEntered] = useState(false)
  const [isLeaved, setIsLeaved] = useState(false)
  const [messages, setMessages] = useState([])
  const [input, setInput] = useState("")
  const stompRef = useRef(null)
  const subscriptionRef = useRef(null)
  const messagesEndRef = useRef(null)

  useEffect(() => {
    const crewInfo = async () => {
      try {
        const res = await jwtAxios.get(`/api/crew/detail/${crewId}`)
        setCrewName(res.data.crewDetail.name)
      } catch (err) {
        console.error("크루 정보 불러오기 실패", err)
      }
    }
    crewInfo()
  }, [crewId])

  // 채팅 입장
  const enterChat = async () => {
    if (!stompRef.current?.connected) return;
    // 최근 대화 300개 불러오기
    try {
      const res = await jwtAxios.get(`/api/mycrew/${crewId}/chat/recent?limit=300`,
        {
          headers: { Authorization: `Bearer ${accessToken}`},
          withCredentials: true
        }
      )
      setMessages(res.data.reverse())
    } catch(err) {
        console.log("대화 로드 실패", err)
    }

    // 구독 따단
    subscriptionRef.current = stompRef.current.subscribe(
      `/topic/chat/crew/${crewId}`, (payload) => {
        const msg = JSON.parse(payload.body)
        setMessages(prev => {
          // 중복 방지
          if (prev.some(m => m.id && m.id === msg.id)) return prev
          return [...prev, msg]
        })
      }
    )
    // 입장 메시지 딴
    stompRef.current.publish({
      destination: `/app/chat/crew/${crewId}/enter`,
      body: JSON.stringify({
        crewId,
        senderId: senderId,
      })
    })

    setIsEntered(true)
  }

  // 채팅 퇴장
  const leaveChat = () => {
    if (!stompRef.current?.connected || !isEntered) return;
    stompRef.current.publish({
      destination: `/app/chat/crew/${crewId}/leave`,
      body: JSON.stringify({
        crewId,
        senderId: senderId,
      })
    })
    // 구독 끝
    subscriptionRef.current?.unsubscribe()
    subscriptionRef.current = null
    setIsLeaved(true)
    setIsEntered(false)
    setMessages([])
  }

  useEffect(() => {
    // stomp 연결
    const socket = new SockJS("http://localhost:8088/ws")
    const stomp = new Client({
      webSocketFactory: () => socket,
      debug: (str) => {console.log(str)},
      onConnect: () => {
        console.log("연결됨")
      },
      onStompError: (err) => {
        console.error("stomp 에러", err)
      }
    })

    stompRef.current = stomp
    stomp.activate()


    return () => {
      // 페이지 이동, 언마운트, 새로고침
      if (stompRef.current?.connected || isLeaved) {
        stompRef.current.publish({
          destination: `/app/chat/crew/${crewId}/leave`,
          body: JSON.stringify({ crewId, senderId })
        })
      }
      stompRef.current?.deactivate()
      stompRef.current = null
      setIsLeaved(true)
    }
  }, [crewId])

  const sendMessage = () => {
    if (!input.trim()) return;
    if (!stompRef.current?.connected || !isEntered) {
      console.log("연결 대기")
      return
    }
    const payload = {
      crewId,
      senderId,
      senderNickName,
      message: input.trim(),
      type: "CHAT"
    }
    // 전송
    stompRef.current?.publish({
      destination: `/app/chat/crew/${crewId}`,
      body: JSON.stringify(payload)
    })
    setInput("")
    console.log(messages)
  }

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({behavior: "smooth"})
  }, [messages])

  const messageType = (msg, index) => {
    const prev = messages[index - 1]
    // 메시지 합체
    const isSameSender = 
      prev &&
      prev.type === "CHAT" &&
      prev.senderId === msg.senderId
    
    if (msg.type === "CHAT") {
      return (
        <div className={`crewMessage ${msg.senderId === senderId ? "me" : "other"}`} 
              key={msg.id}>
          <div className={`profileArea ${isSameSender ? "hidden" : ""}`}>
            {!isSameSender && (
              msg.senderProfileUrl 
                ? <img className='profileImage' src={msg.senderProfileUrl} alt={`${msg.senderId}프로필`} />
                : <div className="replaceProfileEmoji">🏃‍♂️</div>                 
            )}
          </div>
          <div className="chatWrapper">
            {!isSameSender && (
                <strong className='nickName'>{msg.senderNickName}</strong>
            )}
            <div className='amessage'>{msg.message}</div>
          </div>
            <span className='time'>{new Date(msg.createTime).toLocaleString()}</span>
        </div>
      );
    }
    if (msg.type === "ENTER") {
      return (
        <div className="systemMessage" key={`enter.${msg.senderId}.${msg.createTime}`}>
          {msg.message}
        </div>
      )
    }
    if (msg.type === "LEAVE") {
      return (
        <div className="systemMessage" key={`leave.${msg.senderId}.${msg.createTime}`}>
          {msg.message}
        </div>        
      )
    }
  }
  return (
    <div className="crewChat">
      <div className="crewChat-con">
        <div className="crewChat-header">
          <h3>{crewName} 🏃‍♂️ 채팅</h3>
          <div className="chatButton">
            {!isEntered
              ? <button onClick={enterChat}>참여하기</button>
              : <button onClick={leaveChat}>나가기</button>
            }
          </div>
        </div>
        <div className="recentMessages">
          {messages.map((m, i) => messageType(m, i))}
          <div ref={messagesEndRef}></div>
        </div>
        <div className='writeMessage'>
          <input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => { if (e.key === "Enter") sendMessage(); }}
          />
          <button onClick={sendMessage}>전송</button>
        </div>
      </div>
    </div>
  )
}

export default MyCrewChatContainer