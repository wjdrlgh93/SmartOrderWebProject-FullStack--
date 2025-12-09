import React, { useEffect, useState } from 'react'
import { Link } from 'react-router';
import jwtAxios from '../../../apis/util/jwtUtil';

import "../../../css/common/crewModal.css"
import { useSelector } from 'react-redux';


const CrewList = ({children}) => {

    const [crewList, setCrewList] = useState([]);

    const API_BASE_URL = 'http://localhost:8088/api/crew';

     // JWT
    const accessToken = useSelector(state => state.jwtSlice.accessToken);
    const memberId = useSelector(state => state.loginSlice.id);


    const fetchData = async () => {
        const response = await jwtAxios.get(`${API_BASE_URL}/mycrewList/${memberId}`,
     {
                headers: { Authorization: `Bearer ${accessToken}` },
                withCredentials: true,
    });
        console.log(response.data);

    if(response.data){
        setCrewList(response.data);

    } else {
        console.log("게시물 데이터가 존재하지 않음.");
    }
};

    useEffect(() => {
        fetchData();
    }, []);


  return (
    <div className="modal-container">
        {children}
       
        <div className="modal-box-crew-empty">
            {console.log(crewList)}
        </div>
        <div className="modal-box-crew">
            <ul>
                {crewList.length === 0 ? (
                <li 
                    key="no-crew" 
                    style={{ 
                        textAlign: 'center', 
                        opacity: 1, 
                        visibility: 'visible',
                        // .modal-box-crew의 위치 지정 속성을 무력화
                        position: 'static', 
                        transform: 'none',
                        color: '#333' // 배경색에 따라 글자색 조정
                    }}
                >
                    <Link to="/crew/index">
                        현재 가입된 크루가 없습니다. 새로운 크루를 만나보세요!
                    </Link>
                </li>

            ) : crewList.length > 4 ? (
                <>
                {crewList.slice(0, 4).map((list) => (
                    <li className="yes-crew" key={list.id}>
                        <Link to={`/mycrew/${list.crewId}`}>{list.crewName}</Link>
                            {console.log(crewList)}
                    </li>
                ))}
                <li key="more">
                    <Link to="/crew/list" className="more-link">
                        더보기 ({crewList.length - 4}개 더 보기)
                    </Link>
                </li>
            </>
            ) : (
                crewList.map((list) => (
                    <li key={list.id}>
                        {/* <Link to={`/crew/detail/${list.id}`}>{list.name}</Link> */}
                        <Link to={`/mycrew/${list.crewId}`}>{list.crewName}</Link>
                    </li>
                ))
            )}
            </ul>
        </div>
    </div>
  )
}

export default CrewList