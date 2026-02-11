import axios from 'axios'
import React, { useState } from 'react'
//============크루에 가입신청============================================
// ==========CrewDetailIndex.jsx 에 추가 해주세요=============================

//  crewId 받는 useParams() 이미 있다면 크루 가입 데이터에 crewRequestId: 여기 
//  해당 crewId 받는 useParams() 변수로 바꿔주세요
//  const {crewId} = useParams() *--*

// loginMemberId jwt isLogin로그인 아이디. 이미 있다면 크루가입데이터에
// memberRequestId: 여기 해당 jwt isLogin로그인 아이디 받는 변수로 바꿔주세요
// const loginMemberId = useSelector((state) => state.loginSlice.id) *--*

//  크루가입데이터
// const crewJoinRequestData = {
    //     crewRequestId: crewId,
    //     memberRequestId: loginMemberId,
    //     message: "",
    // }
   
// 모달 ,데이터 상태관리
// const [ joinRequestData, setJoinRequestData ] = useState(crewJoinRequestData)
// const [ joinRequestModal, setJoinRequestModal] = useState(false)

// input 데이터로 변환
// const onInputChange = async (e) => {
//     const name = e.target.name;
//     const value = e.target.value;
//     // console.log(name, value);

//     setJoinRequestData({ ...joinRequestData, [name]: value });
// }

//가입 함수
// const onJoinRequest = async () => {
//     try {
//         //CrewsController 없애면서 CrewController 로 옮겨주세요 
//         // post 주소도 바꿔주세요
//         const res = await axios.post(`/api/crew/joinRequest`,
//             joinRequestData,
//             { headers: { "Content-Type": "application/json" }}
//         ) 

//         console.log(res.data)
//     } catch (error) {
    //         alert('크루 가입 요청 보내기 실패')
    //     }
    // }


    //return 

    // 가입 신청 하는 버튼 어딘가에 넣어주세요
    // <button type="button" onClick={()=>setJoinRequestModal(true)}>가입신청</button> 

    // div 사이 어딘가에 넣기 
        // {joinRequestModal && 
        //     (<CrewJoinRequestModal
        //         onCrew={crew}
        //         input={joinRequestData}
        //         onClose={()=> setJoinRequestModal(false)}
        //         onSubmit={onJoinRequest}
        //         onChange={onInputChange}
        //     />)}

 // =====================================================================       

const CrewJoinRequestModal = ({onCrew, input, onClose, onSubmit, onChange }) => {
    //크루 속에 데이터가 비었다면?
    if (!onCrew) return <div>왜 데이터가 없지?</div>
    
  return (
    <div className='crewJoinRequestModal'>
        <div className='crewJoinRequestModal-con'>
            <div className="modal-header">
                <h2>{onCrew.name} 가입신청</h2>
                <button type='button' onClick={onClose}>✖</button>
            </div>
            <div className="modal-body">
            <ul>
                <li>
                    <label htmlFor="message">가입인사말</label>
                    <input type="text" id='message' name="message" value={input.message} onChange={onChange}/>
                </li>
                <button type='button' onClick={onSubmit}>가입신청</button>
            </ul>
            </div>
        </div>
    </div>
  )
}

export default CrewJoinRequestModal