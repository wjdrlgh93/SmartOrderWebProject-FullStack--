import React from 'react'

const MyCrewMemberDetailModal = ({detail, onClose, onDelete}) => {
    //크루원 속에 데이터가 비었다면?
    if (!detail) return <div>🤔 왜 데이터가 없지?</div>

const crewMemberCreateTime = detail.createTime ? detail.createTime.split("T")[0] :  "";

return (
  <div className='myCrewMemberModal'>
    <div className='myCrewMemberModal-con'>
      <div className="myCrewMemberModal-header">
        <h2>👤 {detail.memberId}님의 상세정보</h2>
        <button type='button' onClick={onClose}>✖</button>
      </div>

        <div className='myCrewMemberModal-mid'>
        <div className="myCrewMemberImage">
        
              <img
                src={detail.memberImages && detail.memberImages.length > 0 
                ? detail.memberImages[0]
                : "https://placehold.co/150x150?text=No+Image"}   
                alt=""
                className="memberImage"
              />
        </div>
      <div className="myCrewMemberModal-body">
        <ul>
            <li>🆔 회원아이디 : {detail.memberId}</li>
            <li>🏷️ 회원닉네임 : {detail.memberNickName}</li>
            <li>🛡️ 크루권한 : {detail.roleInCrew}</li>
            <li>📅 가입날짜 : {crewMemberCreateTime}</li>
          <li className='buttons-row'>
            <button type='button' onClick={onDelete} >🚪 탈퇴</button>
            </li>
        </ul>
        </div>
      </div>
    </div>
  </div>
)
}

export default MyCrewMemberDetailModal