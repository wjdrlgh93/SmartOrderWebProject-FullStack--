import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import CrewJoinRequestModal from './CrewJoinRequestModal';
import { useSelector } from 'react-redux';
import jwtAxios from '../../../apis/util/jwtUtil';

const CrewDetailContainer = () => {
  const { crewId } = useParams();
  const [crew, setCrew] = useState({}); // 초기값 null
  const navigate = useNavigate();
  const loginMemberId = useSelector(state => state.loginSlice.id)

  useEffect(() => {
    const fetchCrewDetail = async (crewId) => {
      try {
        const res = await axios.get(`/api/crew/detail/${crewId}`);
        setCrew(res.data.crewDetail);
        console.log(res.data.crewDetail);
      } catch (err) {
        console.error("크루 상세 실패", err);
      }
    };
    fetchCrewDetail(crewId);
  }, [crewId]);
  
  // 크루 가입 데이터
  const crewJoinRequestData = {
    crewRequestId: crewId,
    memberRequestId: loginMemberId,
    message: "",
  }

  // 모달, 데이터 상태관리
  const [ joinRequestData, setJoinRequestData ] = useState(crewJoinRequestData)
  const [ joinRequestModal, setJoinRequestModal ] = useState(false)

  // input 데이터로 변환
  const onInputChange = async (e) => {
    const name = e.target.name;
    const value = e.target.value;
    console.log(name, value)

    setJoinRequestData({ ...joinRequestData, [name]: value });
  }

  // 가입 함수
  const onJoinRequest = async () => {
    if (!loginMemberId) {
      alert("로그인이 필요합니다.")
    }
    if (!window.confirm("크루 가입 신청하시겠습니까?")) return;
    try {
      const res = await jwtAxios.post(`/api/crew/joinRequest`,
        joinRequestData,
        { headers: { "Content-Type": "application/json" }}
      )      
      console.log(res.data)
      setJoinRequestModal(false)
      alert('크루 가입 신청이 완료 되었습니다.')
    } catch (error) {
      alert('크루 가입 요청이 실패했습니다.')
    }
  }  

  return (
    // <CrewDetailLayout>
      <div className="crewDetailHome">
        <div className="crewDetailHome-con">
          <div className="crewDetailImage">
          {crew.newFileName?.length > 0 ? (
              <img
                // src={`http://localhost:8088/upload/${crew.newFileName[0]}`}
                src={crew.fileUrl[0]}
                alt={`${crew.name} 이미지`}
                className='crewImage'
              />
            ) : (
              <div className='noDetailImg'>👟🤝👟</div>
            )}
          </div>
          <div className="introduction">
            <ul className="crewMain-list">            
              <li className="crewMain-row crewMain-row-name">
                <h2>{crew.name}</h2>
              </li>  
            <li className="crewMain-row crewMain-row-desc">
              {crew.description}
            </li>  
            <li className="crewMain-row crewMain-row-district">
              {crew.district}
            </li>
            <li className="crewMain-row crewMain-row-member">
              멤버 {crew.crewMemberEntities?.length ?? 0}명
            </li>
            <li className="crewMain-row crewMain-row-btn">
              <button className='crewJoin-Btn' type='button' onClick={() => setJoinRequestModal(true)}>가입신청</button>
            </li>
          </ul>
          </div>
        </div>
        {joinRequestModal &&
            (<CrewJoinRequestModal
                onCrew={crew}
                input={joinRequestData}
                onClose={() => setJoinRequestModal(false)}
                onSubmit={onJoinRequest}
                onChange={onInputChange}
              />
            )}
      </div>
    // </CrewDetailLayout>
  );
}

export default CrewDetailContainer