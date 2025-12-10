import { Client } from '@stomp/stompjs'
import React, { useEffect, useRef, useState } from 'react'
import SockJS from 'sockjs-client'

const MyCrewBot = ({onCrewId, onMemberId, onNickName}) => {

    if (!onCrewId || !onNickName || !onMemberId) return alert('너 크루원아닌데 어케옴')
    

    const sendSetCrewBotData = {
        crewId: onCrewId,
        memberId: onMemberId,
        memberNickName: onNickName,
        text: "",
    }
    

    const [sendCrewBotData, setSendCrewBotData] = useState(sendSetCrewBotData)
    

    const [displayData, setDisplayData] = useState([])


    const clientRef = useRef(null)


    const subscriptionRef = useRef(null)


    const displayRef = useRef(null)



    const [botMessage, setBotMessage] = useState();
    

    const onChatBotChange = (e) => {
        const name = e.target.name;
        const value = e.target.value;

        setSendCrewBotData({ ...sendCrewBotData, [name]: value });
      };
    

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

            destination: "/app/chatBot", 
            body: JSON.stringify(sendCrewBotData),
          });

        setSendCrewBotData(prev => ({...prev,
            text: "",
        }))
    }


    const onChatBotSub = () => {
        const client = clientRef.current
        if (subscriptionRef.current) {
            return
          }
        client.subscribe(`/topic/crewChatBot/${onCrewId}/${onMemberId}`, (msg) => {

            const msgBody = JSON.parse(msg.body);

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

                    reconnectDelay: 5000,
                    onConnect: () => {
                        console.log(
                        "✅ BOT STOMP 연결성공 (crewId:",onCrewId,
                        ", nickName:",onNickName,
                        ", memberId:",onMemberId,")")
                        

                        onChatBotSub()
                        

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
                          

                        client.publish({
                            destination: "/app/chatBot",
                            body: JSON.stringify(helloMsg),
                          })

                    }
                });

                clientRef.current = client 


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