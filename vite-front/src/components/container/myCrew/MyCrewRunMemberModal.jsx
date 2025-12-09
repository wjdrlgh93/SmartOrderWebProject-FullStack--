import React, { useState } from 'react'

const MyCrewRunMemberModal = ({input, onClose, onMember,runId, nowPage, startPage, endPage, totalPages}) => {

  return (
    <div className='myCrewRunMemberModal'>
      <div className='myCrewRunMember-con'>
        <div className="myCrewRunMember-header">
          <div><h2>👥 크루런닝 스케줄 참가자리스트</h2></div>
          <button type='button' onClick={onClose}>✖</button>
        </div>
        <div className="myCrewRunMember-body">
          {/* 필요한 정보는 더 넣으면 됨 근데 일단 걍 이것만 넣음 */}
          <ul>
            <li>
              <span>📸 프로필</span>
              <span>🔢 참가순서</span>
              <span>👤 회원 ID</span>
              <span>🏷️ 회원 닉네임</span>
            </li>
          </ul>
          <ul>
            {input.map((runMember) => (
              <li key={runMember.id}>
                <span>
                  <img
                src={runMember.memberImages && runMember.memberImages.length > 0 
                  ? runMember.memberImages[0]
                  : "https://placehold.co/150x150?text=No+Image"}   
                  alt=""
                  className="memberImage"
                  />
                </span>
                {/* 넣고 싶은 정보 더 넣으면 됨 dto에 안한거임 */}
                <span>{runMember.id}</span>
                <span>{runMember.memberId}</span>
                <span>{runMember.memberNickName}</span>
              </li>
            ))}
          </ul>
        </div>
        <div className="myCrew-paging">
          <div className="myCrew-paging-con">
            <ul>
              <li>
                <span>📄 총페이지 {totalPages}</span>
                <button
                  disabled={nowPage === 1}
                  onClick={() => onMember(runId, nowPage - 2)}>
                  ⬅ 이전
                </button>
              </li>
  
              <li>
                {Array.from(
                  { length: endPage - startPage + 1 },
                  (_, idx) => startPage + idx
                ).map((pageNum) => (
                  <button
                    key={pageNum}
                    onClick={() => onMember(runId, pageNum - 1)}
                    className={pageNum === nowPage ? "now" : ""}
                  >
                    {pageNum}
                  </button>
                ))}
              </li>
  
              <li>
                <button
                  disabled={nowPage === totalPages}
                  onClick={() => onMember(runId, nowPage)}>
                  다음 ➡
                </button>
              </li>
  
            </ul>
          </div>
        </div>
      </div>
    </div>
  )
}

export default MyCrewRunMemberModal