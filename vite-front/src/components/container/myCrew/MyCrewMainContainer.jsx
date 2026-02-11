import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import jwtAxios from '../../../apis/util/jwtUtil';
import { useSelector } from 'react-redux';
import MyCrewBot from './MyCrewBot';
import { IMAGES_S3_URL } from '../../../apis/commonApis';

const MyCrewMainContainer = () => {
  const accessToken = useSelector(state => state.jwtSlice.accessToken);
  const {crewId} = useParams()
  const [myCrew , setMyCrew] = useState({})

  const navigate = useNavigate()
  const loginMemberId = useSelector((state) => state.loginSlice.id)  
  const loginNickName = useSelector((state) => state.loginSlice.nickName)  

  useEffect(()=> {
    const myCrewMain = async () => {
      try {
        const res = await jwtAxios.get(`/api/mycrew/${crewId}`,
          {
            headers: { Authorization: `Bearer ${accessToken}`},
            withCredentials: true
          }
        );

        console.log(res.data.crew)
        setMyCrew(res.data.crew)

      } catch (error) {
        if (error.response) {
          // console.log("백엔드 응답:", error.response.data)
      
          
          const data = error.response.data
      
          
          const msg = data?.message || "알 수 없는 오류가 발생했습니다."
      
          alert(msg)
        } 
      }
    }
    myCrewMain();
  }, [])

  //안하면 데이터 오기전에 있어서 에러남
  const CrewcreatedDate = myCrew.createTime
  ? myCrew.createTime.split("T")[0]
  : "";

  const crewMemberLength =  myCrew.crewMemberEntities
  ? myCrew.crewMemberEntities.length
  : "";

  // console.log(myCrew.memberId)
  return (
    <div className="myCrewMain">
      <div className="myCrewMain-con">
        {/* ===== 상단 타이틀 ===== */}
        <div className="myCrewMain-title">
          <div className="myCrewMain-title-left">
            <h2 className="crew-name">
              🏃‍♀️ {myCrew.name || "크루 이름"}
            </h2>
            <p className="crew-district">
              📍 {myCrew.district || "활동 지역 미정"}
            </p>
          </div>

          <div className="myCrewMain-title-right">
            <div className="title-badge">
              <span className="badge-label">📅 창단</span>
              <strong className="badge-value">
                {CrewcreatedDate || "-"}
              </strong>
            </div>
            <div className="title-badge">
              <span className="badge-label">👥 크루원</span>
              <strong className="badge-value">
                {crewMemberLength}명
              </strong>
            </div>
          </div>
        </div>

        {/* ===== 내부 내용 ===== */}
        <div className="myCrewMain-inner">
          <div className="myCrewMain-inner-left">
            <div className="chat-bot">
              <MyCrewBot onCrewId={crewId} onMemberId={loginMemberId} onNickName={loginNickName}/>
            </div>
          </div>
          {/* 정보 리스트 */}
          <div className="myCrewMain-inner-right">
            <div className="top">
              {/* 이미지 */}
              {myCrew.newFileName && myCrew.newFileName.length > 0 && (
                <div className="myCrewMainImage">
                  <img
                    // src={`http://localhost:8088/upload/${myCrew.newFileName[0]}`}
                    src={myCrew.fileUrl[0]}
                    // src={`${IMAGES_S3_URL}${myCrew.newFileName[0]}`}
                    alt={`${myCrew.name} 이미지`}
                    className="crewImage"
                  />
                </div>
              )}

            </div>
            <div className="bottom">
              <ul>

                <li className="myCrewMain-row">
                  <span className="row-label">👑 크루장</span>
                  <span className="row-value">
                    {myCrew.memberNickName || "미정"}
                  </span>
                </li>

                <li className="myCrewMain-row">
                  <span className="row-label">📍 활동 지역</span>
                  <span className="row-value">
                    {myCrew.district || "미정"}
                  </span>
                </li>

                <li className="myCrewMain-row">
                  <span className="row-label">📝 소개</span>
                  <span className="row-value">
                    {myCrew.description || "아직 소개글이 없습니다."}
                  </span>
                </li>
                {myCrew.memberId === loginMemberId && (
                  <li className="myCrewMain-row-btn">
                    <button
                      className="myCrewMain-editBtn"
                      onClick={() => navigate(`/mycrew/${crewId}/update`)}>
                      ✏️ 크루 정보 수정
                    </button>
                  </li>
                )}
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};


export default MyCrewMainContainer