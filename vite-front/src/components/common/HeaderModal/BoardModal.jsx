import React from 'react'


import { Link } from 'react-router';


const BoardModal = ({ children }) => {
  return (
    <div className="modal-container" >
        {children}
        <div className="modal-box">
            <ul>
                <li>
                    <Link to="/board/index">커뮤니티게시판</Link>
                </li>
                <li>
                    <Link to="/notice">공지사항</Link>
                </li>
            </ul>
        </div>
    </div>
  )
}

export default BoardModal