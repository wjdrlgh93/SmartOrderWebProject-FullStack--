import axios from 'axios'
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";
import MyCrewRunAddBtnModal from './MyCrewRunAddBtnModal';
import MyCrewRunDetailModal from './MyCrewRunDetailModal';
import MyCrewRunMemberModal from './MyCrewRunMemberModal';
import { useSelector } from 'react-redux';
import jwtAxios from '../../../apis/util/jwtUtil';

const MyCrewRunContainer = () => {
  const accessToken = useSelector(state => state.jwtSlice.accessToken);
  const {crewId} = useParams()

  const loginMemberId = useSelector((state) => state.loginSlice.id)

  //크루런닝일정 생성 데이터
  const crewRunCreateData = {
    crewId: crewId,
    memberId: loginMemberId,
    title: "",
    startAt: "",
    endAt: "",
    place: "",
    routeHint: "" 
  }

  //크루런닝일정 수정 데이터
  const crewRunUpdateeData = {
    id: "",
    crewId: "",
    memberId: "",
    title: "",
    startAt: "",
    endAt: "",
    place: "",
    routeHint: "" 
  }

  //런닝 일정 리스트
  const [myCrewRunData , setMyCrewRunData] = useState([])

  //일정 추가버튼 add
  const [addRunBtnModal, setAddRunBtnModal] = useState(false)

  //일정 날자클릭 add
  const [addRunDateModal, setAddRunDateModal] = useState(false)

  //input->change 를 위한
  const [createRunData, setCreateRunData ] = useState(crewRunCreateData) //일정 추가
  const [updateRunData, setUpdateRunData ] = useState(crewRunUpdateeData) //일정 수정

  //크루런닝 스케줄 상세보기
  const [myCrewRunDetailModal , setMyCrewRunDetailModal] = useState(false)

  //런닝 일정 참가자 리스트
  const [myCrewRunMember , setMyCrewRunMember] = useState([])
  const [myCrewRunMemberModal, setMyCrewRunMemberModal] = useState(false)
  
  //페이징
  const [nowPage, setNowPage] = useState()
  const [startPage, setStartPage] = useState()
  const [endPage, setEndPage] = useState()
  const [totalPages, setTotalPages] = useState()


  // 크루런닝 일정 리스트
  const myCrewRun = async () => {
    try {
      const res = await jwtAxios.get(`/api/mycrew/${crewId}/run`,
        { headers: { 
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json" 
        } 
      }
      )
      // console.log(res.data.crewRun)
      //fullCalendar가 이해 할 수 있는 형태로 변환
      const crewRunList = res.data.crewRun.map((el)=>({
        id: el.id,
        title: el.title,
        start: el.startAt,
        end: el.endAt,
        extendedProps: {
          place: el.place,
          routeHint: el.routeHint,
          crewId: el.crewId,
          memberId: el.memberId
        }
      }))

      setMyCrewRunData(crewRunList)
      
    } catch (error) {
      if (error.response) {
        // console.log("백엔드 응답:", error.response.data)
        const data = error.response.data    
        const msg = data?.message || "알 수 없는 오류가 발생했습니다."
        alert(msg)
      }
    }
  }
  
  //create input을 바꿔주는
  const onInputCreateChange = (e) => {
    const name = e.target.name;
    const value = e.target.value;
    // console.log(name, value);

    setCreateRunData({ ...createRunData, [name]: value });
  };

  //update input을 바꿔주는
  const onInputUpdateChange = (e) => {
    const name = e.target.name;
    const value = e.target.value;
    // console.log(name, value);

    setUpdateRunData({ ...updateRunData, [name]: value });
  };

  useEffect(()=> {
    myCrewRun();
  }, [])

  
  //크루런닝 스케줄 만들기
  const onMyCrewRunCreate = async () =>{
    
    try {
      const res = await jwtAxios.post(`/api/mycrew/${crewId}/run/create`,
           createRunData ,
          { headers: { 
            "Authorization": `Bearer ${accessToken}`,
            "Content-Type": "application/json" 
          } 
        }
        )
        // console.log(res.data.crewRun)
        
        alert('런닝스케줄 만들기 성공')
      } catch (error) {
        if (error.response) {
          // console.log("백엔드 응답:", error.response.data)
          const data = error.response.data    
          const msg = data?.message || "알 수 없는 오류가 발생했습니다."
          alert(msg)
        }
      }
      setAddRunBtnModal(false)
      setCreateRunData(crewRunCreateData)
      myCrewRun();
  }
    //크루런닝 스케줄 수정
  const onMyCrewRunUpdate =async () =>{

    try {
      const res = await jwtAxios.post(`/api/mycrew/${crewId}/run/update`,
         updateRunData,
         { headers: { 
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json" 
        } 
      }

      )
      // console.log(res.data)
      
    } catch (error) {
      if (error.response) {
        // console.log("백엔드 응답:", error.response.data)
        const data = error.response.data    
        const msg = data?.message || "알 수 없는 오류가 발생했습니다."
        alert(msg)
      }
    }
    alert('런닝스케줄 수정 성공')
    setMyCrewRunDetailModal(false)
    setUpdateRunData(crewRunUpdateeData)
    myCrewRun();   
  }

  // 크루런닝 스케줄 상세보기
  const onMyCrewRunDetail = async (info) => {
    const runId = info.event.id
    try {
      const res = await jwtAxios.get(`/api/mycrew/${crewId}/run/detail/${runId}`,
        { headers: { 
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json" 
        } 
      }
      )
      // console.log(res.data)
      const detail = res.data.crewRun
      
      setUpdateRunData({
        id: detail.id,
        crewId: detail.crewId,
        memberId: detail.memberId,
        title: detail.title,
        startAt: detail.startAt,
        endAt: detail.endAt,
        place: detail.place,
        routeHint: detail.routeHint,
      })
      
    } catch (error) {
      if (error.response) {
        // console.log("백엔드 응답:", error.response.data)
        const data = error.response.data    
        const msg = data?.message || "알 수 없는 오류가 발생했습니다."
        alert(msg)
      }
    }
    setMyCrewRunDetailModal(true)
  }
  
  //크루런닝 스케줄 삭제
  const onMyCrewRunDelete =async (runId) =>{
  
    try {
      const res = await jwtAxios.delete(`/api/mycrew/${crewId}/run/delete/${runId}`,
        { headers: { 
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json" 
        } 
      }
      )
      // console.log(res.data)
      
      alert("런닝스케줄 삭제 성공")
    } catch (error) {
      if (error.response) {
        // console.log("백엔드 응답:", error.response.data)
        const data = error.response.data    
        const msg = data?.message || "알 수 없는 오류가 발생했습니다."
        alert(msg)
      }
      
    }
    setMyCrewRunDetailModal(false)
    myCrewRun();  
  }

  // 크루런닝 스케줄 참가자 리스트
  const onMyCrewRunMember = async (runId,pageParam) => {
    // console.log(" onMyCrewRunMember 호출", { runId, pageParam });
    try {
      const res = await jwtAxios.get(`/api/mycrew/${crewId}/run/${runId}/member`,
        {
          params: {
            page: pageParam
          }
        },
        { headers: { 
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json" 
        } 
      })
      // console.log(res.data)
      setMyCrewRunMember(res.data.crewRunMember.content)
      setNowPage(res.data.nowPage)
      setStartPage(res.data.startPage)
      setEndPage(res.data.endPage)
      setTotalPages(res.data.totalPages)
    } catch (error) {
      if (error.response) {
        // console.log("백엔드 응답:", error.response.data)
        const data = error.response.data    
        const msg = data?.message || "알 수 없는 오류가 발생했습니다."
        alert(msg)
      }
    }
    setMyCrewRunMemberModal(true)
  }

  //크루런닝 스케줄 참가
  const onMyCrewRunMemberYes = async (runId,memberId) => {
    
    try {
      const res = await jwtAxios.post(`/api/mycrew/${crewId}/run/${runId}/member/${memberId}/yes`,
        { headers: { 
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json" 
        } 
      }
      )
      // console.log(res.data)
      
      alert("크루런닝 스케줄 참가 성공")
    } catch (error) {
      if (error.response) {
        // console.log("백엔드 응답:", error.response.data)
        const data = error.response.data    
        const msg = data?.message || "알 수 없는 오류가 발생했습니다."
        alert(msg)
      }
    }
    onMyCrewRunMember(runId);
    
  }

   //크루런닝 스케줄 참가취소
  const onMyCrewRunMemberNo = async (runId,memberId) => {
    
    try {
      const res = await jwtAxios.delete(`/api/mycrew/${crewId}/run/${runId}/member/${memberId}/no`,
        { headers: { 
          "Authorization": `Bearer ${accessToken}`,
          "Content-Type": "application/json" 
        } 
      }
      )
      // console.log(res.data)
      
      alert("크루런닝 스케줄 참가 취소 성공")
    } catch (error) {
      if (error.response) {
        // console.log("백엔드 응답:", error.response.data)
        const data = error.response.data    
        const msg = data?.message || "알 수 없는 오류가 발생했습니다."
        alert(msg)
      }
    }
    onMyCrewRunMember(runId);
    
  }
  
  
  return (
    <div className='myCrew'>
      <div className='myCrew-con'>
        <div>
          <h2>🏃‍♀️ 크루런닝스케줄</h2>
        </div>
        <div className="myCrew-calendar">
          <FullCalendar
            height={650}
            locale={"kr"}
            plugins={[dayGridPlugin, interactionPlugin]} //npm 으로 다운받은거
            initialView="dayGridMonth"  // 첫 화면: 월간 달력
            customButtons={{
              addCrewRunButton: {
                text: "🏃‍♂️ 런닝 스케줄 추가",
                click: () => setAddRunBtnModal(true)
              },
            }}           
            headerToolbar={{
              left: "prev,next today",
              center: "title",
              right: "addCrewRunButton",
            }}
            selectable={true}
            dateClick={null}           // 날짜 클릭 이벤트
            events={myCrewRunData}       // 일정 데이터
            eventClick={onMyCrewRunDetail} //일정 클릭이벤트
          />
        </div>
      </div>
      {/* 추가버튼클릭 런닝스케줄 추가 모달 */}
      {/* 모르겠는거는 ??={안에이거} <= 컨트롤 클릭으로 뭐하는 애인지 보삼 */}
      {addRunBtnModal && (
        <MyCrewRunAddBtnModal
          input={createRunData}
          onClose={() => {
            setAddRunBtnModal(false)
            myCrewRun()
          }}
          onChange={onInputCreateChange}
          onSubmit={onMyCrewRunCreate}
        />
      )}
  
      {/* 크루런닝 일정 상세보기, 수정, 일정 참가, 일정 참가 취소 */}
      {/* 모르겠는거는 ??={안에 이거} <= 컨트롤 클릭으로 뭐하는 애인지 보삼 */}
      {myCrewRunDetailModal && (
        <MyCrewRunDetailModal
          input={updateRunData}
          onClose={() => {
            setMyCrewRunDetailModal(false)
            setMyCrewRunMemberModal(false)
            myCrewRun()
          }}
          loginId={loginMemberId}
          onChange={onInputUpdateChange}
          onSubmit={onMyCrewRunUpdate}
          onDelete={onMyCrewRunDelete}
          onMember={onMyCrewRunMember}
          onRunYes={onMyCrewRunMemberYes}
          onRunNo={onMyCrewRunMemberNo}
          nowPage={0}
        />
      )}
  
      {/* 일정 참가 크루원보기 */}
      {/* 모르겠는거는 ??={안에이거} <= 컨트롤 클릭으로 뭐하는 애인지 보삼 */}
      {myCrewRunMemberModal && (
        <MyCrewRunMemberModal
          input={myCrewRunMember}
          onClose={() => setMyCrewRunMemberModal(false)}
          runId={updateRunData.id}
          onMember={onMyCrewRunMember}
          nowPage={nowPage}
          startPage={startPage}
          endPage={endPage}
          totalPages={totalPages}
        />
      )}
  
      {/* 얘는 일단 안함... 할지말지 고민 */}
      {/* 날짜클릭 런닝스케줄 추가 모달 */}
      {/* 모르겠는거는 ??={안에이거} 컨트롤 클릭으로 뭐하는 애인지 보삼 */}
      {/* {addRunDateModal && (
        <MyCrewRunAddBtnModal
        detail={detailData}
        onClose={()=> {
          setAddRunDateModal(false)
          setDetailData(null)
          myCrewRun()}}
        onCreate={()=>onMyCrewRunCreate(crewMemberTbId)}
        onChange={()=>onInputChange(e)}
        />
      )} */}
    </div>
  )
  
}

export default MyCrewRunContainer