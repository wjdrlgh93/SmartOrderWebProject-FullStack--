import React, { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'
import { NavLink, useParams } from 'react-router-dom'
import jwtAxios from '../../../apis/util/jwtUtil'

const MyCrewLeftContainer = () => {
  const {crewId} = useParams()
  const accessToken = useSelector(state => state.jwtSlice.accessToken);
  const loginMemberId = useSelector((state) => state.loginSlice.id) 
   const [crewLeader , setCrewLeader ] = useState({})
   useEffect(()=> {
    const myCrewLeft = async () => {
      try {
        const res = await jwtAxios.get(`/api/mycrew/${crewId}`,
          {
            headers: { Authorization: `Bearer ${accessToken}`},
            withCredentials: true
          }
        );
        // console.log(loginMemberId)
        // console.log(res.data.crew.memberId)
        setCrewLeader(res.data.crew.memberId)

      } catch (error) {
        if (error.response) {
          // console.log("백엔드 응답:", error.response.data)
          
          const data = error.response.data
      
          const msg = data?.message || "알 수 없는 오류가 발생했습니다."
      
          alert(msg)
        } 
      }
    }
    myCrewLeft();
  }, [])


  return (
    <div className='myCrewLeftContainer'>
      <div className='myCrewLeftContainer-con'>
        <ul>
          <li>
            <NavLink 
              to={"index"} 
              className={({ isActive }) => (isActive ? "left-now" : "")}
            >
              🏠 CREW HOME
            </NavLink>
          </li>
          {crewLeader === loginMemberId && 
          <>
            <li>
              <NavLink 
                to={"join"}
                className={({ isActive }) => (isActive ? "left-now" : "")}
                >
                ✅ 가입신청명단
              </NavLink>
            </li>
    
            <li>
              <NavLink 
                to={"member"} 
                className={({ isActive }) => (isActive ? "left-now" : "")}
                >
                👥 크루원
              </NavLink>
            </li>
            </>
            }
  
          <li>
            <NavLink 
              to={"run"} 
              className={({ isActive }) => (isActive ? "left-now" : "")}
            >
              🏃‍♀️ 런닝스케줄
            </NavLink>
          </li>
  
          <li>
            <NavLink 
              to={"board"} 
              className={({ isActive }) => (isActive ? "left-now" : "")}
            >
              📝 크루게시글
            </NavLink>
          </li>
  
          <li>
            <NavLink 
              to={"chat"} 
              className={({ isActive }) => (isActive ? "left-now" : "")}
            >
              💬 크루채팅
            </NavLink>
          </li>
        </ul>
      </div>
    </div>
  )
  
}

export default MyCrewLeftContainer