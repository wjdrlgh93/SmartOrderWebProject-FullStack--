import { Client } from '@stomp/stompjs'
import React, { useEffect, useRef, useState } from 'react'
import SockJS from 'sockjs-client'

const MyCrewBot = ({onCrewId, onMemberId, onNickName}) => {
    // 셋중 뭐라도 없으면 나가라
    if (!onCrewId || !onNickName || !onMemberId) return alert('너 크루원아닌데 어케옴')
    
    // 쳇봇이랑 주고받는 데이터
    const sendSetCrewBotData = {
        crewId: onCrewId,
        memberId: onMemberId,
        memberNickName: onNickName,
        text: "",
    }
    
    // 쳇봇에게 전송할때 가는 데이터 저장 state
    const [sendCrewBotData, setSendCrewBotData] = useState(sendSetCrewBotData)
    
    // 채팅창에서 봇 = 왼쪽, 사람 = 오른쪽 나눌려고 만들었어요 저장 state
    const [displayData, setDisplayData] = useState([])

    //클라이언트 정보 Ref로저장
    const clientRef = useRef(null)

    //구독정보 Ref로저장
    const subscriptionRef = useRef(null)

    //쳇봇 디스플레이 스크롤용  
    const displayRef = useRef(null)

    // 이것도 봇메세지인데 처음에 확인용으로 만들었는데 
    // 나중에 봇메시지만 보는걸 만들까말까 고민하면서 남김
    const [botMessage, setBotMessage] = useState();
    
    // onChange
    const onChatBotChange = (e) => {
        const name = e.target.name;
        const value = e.target.value;
        // console.log(name, value);
        setSendCrewBotData({ ...sendCrewBotData, [name]: value });
      };
    
    // 쳇봇에게 질문하기 
    const onChatBotSend = () => {
        const client = clientRef.current
        if (!sendCrewBotData.text) {
            alert('메시지를 입력하세요')
        }
        setDisplayData(prev => [...prev,
            {
              id: onCrewId,
              text: sendCrewBotData.text,
              sender: "USER",
            },])
        
        client.publish({
            // 컨트롤러에 @MessageMapping("/chatBot")
            destination: "/app/chatBot", 
            body: JSON.stringify(sendCrewBotData),
          });
        // console.log("컨트롤러로 보냄")
        setSendCrewBotData(prev => ({...prev,
            text: "",
        }))
    }

    //rabbitmq로 오는거
    const onChatBotSub = () => {
        const client = clientRef.current
        if (subscriptionRef.current) {
            return
          }
        client.subscribe(`/topic/crewChatBot/${onCrewId}/${onMemberId}`, (msg) => {
            //온거에서 dto data만 변수에 담기
            const msgBody = JSON.parse(msg.body);
            // console.log("🤖 봇 응답 도착:", msgBody);
            setBotMessage(msgBody); //state에 저장
           
            setDisplayData(prev => [...prev,
                {
                  id: onCrewId,
                  text: msgBody.text,
                  sender: "BOT",
                },])
        })
    }

    const myCrewBotConnect = async () => {
        try {
            const socket = new SockJS("/ws");
                    const client = new Client({
                    webSocketFactory: () => socket,
                    //끊어지면 재연결 5초 뭐더라 
                    reconnectDelay: 5000,
                    onConnect: () => {
                        console.log(
                        "✅ BOT STOMP 연결성공 (crewId:",onCrewId,
                        ", nickName:",onNickName,
                        ", memberId:",onMemberId,")")
                        
                        //crewId로 메시지오게 문?열기
                        onChatBotSub()
                        
                        //어서오고
                        const helloMsg = {
                            crewId: onCrewId,
                            memberId: onMemberId,
                            memberNickName: onNickName,
                            text: "hellow",
                          }
                        
                        setDisplayData(prev => [...prev,
                            {
                            id: onCrewId,
                            text: helloMsg.text,
                            sender: "USER",
                            },])
                          
                        //컨트롤러로 슈웃    
                        client.publish({
                            destination: "/app/chatBot",
                            body: JSON.stringify(helloMsg),
                          })
                        //   console.log("🚀 초기 hellow 보냄", helloMsg)
                    }
                });
                //클라이언트 정보 Ref에담기
                clientRef.current = client 

                //이게 STOMP연결이라는데 이거 쓰라길래 씀
                client.activate(); 
                
            } catch (error) {   
                console.log("연결실패")
            }
        }
        
    useEffect(()=>{
        if (!clientRef.current) {
            myCrewBotConnect();
          }       
        const el = displayRef.current
        el.scrollTop = el.scrollHeight
    }, [displayData])

    const comeOnBotMessage = botMessage ? botMessage.text : "";
    const sendOnBotMessage = sendCrewBotData ? sendCrewBotData.text : "";
    
  return (
    <div className="myCrewBot">
        <div className="myCrewBot-con">
            <div className="myCrewBot-display" ref={displayRef}>
                {/* <div>{comeOnBotMessage}테스트</div> */}
                
            {displayData.length > 0 ? (
                displayData.map((msg, idx) => (
                <ul key={idx}>
                    {msg.sender === "BOT" 
                    ? <li className='Bot'>🤖 {msg.text}</li>
                    : <li className='User'>{msg.text} 🧍</li>
                    }
                </ul>
                ))
            ) : (
                <div>아직 대화가 없습니다.</div>
            )} 
            </div>
            <div className="myCrewBot-input">
            <input
                type="text"
                name="text"
                value={sendOnBotMessage}
                onChange={onChatBotChange}
                onKeyDown={(e)=>{
                    if (e.key === "Enter") {
                        onChatBotSend();
                      }
                }}
                placeholder="봇에게 보낼 메시지"
            />
                <button type='button' onClick={onChatBotSend}>전송</button>
            </div>
        </div>
    </div>
  )
}

export default MyCrewBot