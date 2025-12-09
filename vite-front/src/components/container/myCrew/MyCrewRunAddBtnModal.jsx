import React from 'react'

const MyCrewRunAddBtnModal = ({input, onClose, onChange, onSubmit}) => {
    
    return (
        <div className='myCrewModal'>
          <div className='myCrewModal-con'>
            <div className="modal-header">
              <h2>🏃‍♀️ 크루런닝 일정추가</h2>
              <button type='button' onClick={onClose}>✖</button>
            </div>
            <div className="modal-body">
              <ul>
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
                <li className='buttons-row'>
                  <button type='button' onClick={onSubmit}>✅ 일정추가</button>
                </li>
              </ul>
            </div>
          </div>
        </div>
      )
      
}

export default MyCrewRunAddBtnModal