import React from 'react'

const MyCrewRunDetailModal = ({input, onClose, loginId, onChange, onSubmit, onDelete, onMember, onRunYes, onRunNo, nowPage}) => {
    
    return (
        <div className='myCrewModal'>
          <div className='myCrewModal-con'>
            <div className="modal-header">
              <h2>📋 {input.title} 상세보기</h2>
              <button type='button' onClick={onClose}>✖</button>
            </div>
            <div className="modal-body">
              <ul>
                <li>
                  {/* 크루런닝일정ID */}
                  <input
                    type="text"
                    id='id'
                    name="id"
                    value={input.id}
                    onChange={onChange}
                    hidden
                  />
                </li>
                <li>
                  {/* 크루ID */}
                  <input
                    type="text"
                    id='crewId'
                    name="crewId"
                    value={input.crewId}
                    onChange={onChange}
                    hidden
                  />
                </li>
                <li>
                  <label htmlFor="startAt">📅 시작날짜</label>
                  <input
                    type="datetime-local"
                    id='startAt'
                    name="startAt"
                    value={input.startAt}
                    onChange={onChange}
                  />
                </li>
                <li>
                  <label htmlFor="endAt">⌛ 종료날짜</label>
                  <input
                    type="datetime-local"
                    id='endAt'
                    name="endAt"
                    value={input.endAt}
                    onChange={onChange}
                  />
                </li>
                <li>
                  <label htmlFor="title">📝 모임 제목</label>
                  <input
                    type="text"
                    id='title'
                    name="title"
                    value={input.title}
                    onChange={onChange}
                  />
                </li>
                <li>
                  <label htmlFor="place">📍 모임 장소</label>
                  <input
                    type="text"
                    id='place'
                    name="place"
                    value={input.place}
                    onChange={onChange}
                  />
                </li>
                <li>
                  <label htmlFor="routeHint">🗺️ 짧은 코스</label>
                  <input
                    type="text"
                    id='routeHint'
                    name="routeHint"
                    value={input.routeHint}
                    onChange={onChange}
                  />
                </li>
                <li>
                  <label htmlFor="memberId">👤 일정 주최자</label>
                  <input
                    type="text"
                    id='memberId'
                    name="memberId"
                    value={input.memberId}
                    onChange={onChange}
                    readOnly
                  />
                </li>
                <li className='detail-btn-list'>
                  <button type='button' onClick={onSubmit}>✏️ 수정</button>
                  <button type='button' onClick={() => onMember(input.id, nowPage)}>👥 참가 크루원</button>
      
                  {/* 로그인 유저 아이디 받아서 해야하는데 일단 임시로 그냥 함 MyCrewRunContainer 맨위에 있음*/}
                  <button type='button' onClick={() => onRunYes(input.id, loginId)}>✅ 참가</button>
                  <button type='button' onClick={() => onRunNo(input.id, loginId)}>🚫 참가 취소</button>
      
                  {input.memberId === loginId && (  // 일정 생성자와, 로그인 아이디가 같다면 
                    <button type='button' onClick={() => onDelete(input.id)}>🗑️ 삭제</button>
                  )}
                </li>
              </ul>
            </div>
          </div>
        </div>
      )
}

export default MyCrewRunDetailModal