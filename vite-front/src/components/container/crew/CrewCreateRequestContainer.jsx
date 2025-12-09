import axios from 'axios';
import React, { useEffect, useRef, useState } from 'react'
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import jwtAxios from '../../../apis/util/jwtUtil';

const CrewCreateRequestContainer = () => {
  const navigate = useNavigate();
  const { isLogin } = useSelector((state) => state.loginSlice);
  const { accessToken } = useSelector((state) => state.jwtSlice);

  const [crewName, setCrewName] = useState("");
  const [message, setMessage] = useState("");
  const [district, setDistrict] = useState("");
  const [responseMsg, setResponseMsg] = useState("");

  const firstRender = useRef(true);

  const districtsInSeoul = [
    "강남구", "강동구", "강북구", "강서구", "관악구",
    "광진구", "구로구", "금천구", "노원구", "도봉구",
    "동대문구", "동작구", "마포구", "서대문구", "서초구",
    "성동구", "성북구", "송파구", "양천구", "영등포구",
    "용산구", "은평구", "종로구", "중구", "중랑구"
  ];

  const selectChange = (el) => {
    setDistrict(el.target.value);
  }

  useEffect(() => {
    if (firstRender.current && !isLogin) {
        firstRender.current = false;
        
        const goLogin = window.confirm(
            "로그인이 필요합니다.\n로그인 페이지로 이동하시겠습니까?"
        );

        if (goLogin) {
            navigate("/auth/login");
        } else {
            const goJoin = window.confirm(
                "아직 회원이 아니신가요?\n회원가입을 하시면 다양한 서비스를 이용하실 수 있습니다.\n회원가입 페이지로 이동하시겠습니까?"
            );
            if (goJoin) {
                navigate("/auth/join")
            }
        }
    }
  }, [isLogin, navigate]);

  
  const submit = async (el) => {
    el.preventDefault();
      
    if (!isLogin) return;
    if (!window.confirm('크루 신청하시겠습니까?')) return;

    try {
      const response = await jwtAxios.post("/api/crew/create/request", 
        {
            crewName,
            message,
            district,
        },
        {
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${accessToken}`,
            },
            withCredentials: true,
        }        
      );
      setResponseMsg(response.data.message)
      alert("크루 신청이 완료 되었습니다.")
    } catch (err) {
        alert("크루 신청 실패")
        console.error(err);
        setResponseMsg("오류 발생")
    } 
  }

  if (!isLogin) return null;

  return (
    <div className="createRequest">
        <div className="createRequest-con">
            <h2>크루 신청</h2>
            {isLogin ? (
                <form onSubmit={submit}>
                    <div className="crewInfo">
                        <label className="crewName">크루 이름</label>
                        <input 
                            type="text" 
                            value={crewName} 
                            onChange={(e) => setCrewName(e.target.value)}
                            required
                            placeholder="크루 이름"
                        />
                    </div>
                    <div className="requestMessage">
                        <label className="createMessage">크루 소개</label>
                        <textarea 
                            name="message" 
                            id="message"
                            value={message}
                            onChange={(el) => setMessage(el.target.value)}
                            required
                            placeholder="크루 소개"
                        />
                    </div>
                    <div className="district">
                        <label className="crewDistrict">지역 선택</label>
                        <select name="district" id="district" value={district} onChange={selectChange}>
                            <option value="">지역선택</option>
                            {districtsInSeoul.map((d, index) => (
                                <option key={index} value={d}>
                                    {d}
                                </option>
                            ))}
                        </select>
                        {district && <p>선택한 지역: {district}</p>}                 
                    </div>
                    <div className="createRequestBtn">
                        <button className="submitBtn" type="submit">신청하기</button>
                    </div>
                </form>
            ) : (
                <p className="loginRequired">로그인이 필요합니다.</p>
            )}
            <p className="responseMsg">{responseMsg}</p>
        </div>
    </div>
  );
}

export default CrewCreateRequestContainer