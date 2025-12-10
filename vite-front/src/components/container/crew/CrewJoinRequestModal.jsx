import axios from 'axios'
import React, { useState } from 'react'

















   















































const CrewJoinRequestModal = ({onCrew, input, onClose, onSubmit, onChange }) => {

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