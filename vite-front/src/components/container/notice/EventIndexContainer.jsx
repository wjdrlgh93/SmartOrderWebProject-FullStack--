import React from 'react'

import "../../../css/event/eventMain.css"

// Dummy
const events = [
  { id: 1, title: '봄맞이 러닝 축제 2025', date: '2025.04.20', location: '서울숲' },
  { id: 2, title: '도심 속 요가 & 명상 클래스', date: '2025.04.25', location: '남산 공원' },
  { id: 3, title: '한강 야경 자전거 레이스', date: '2025.05.05', location: '한강 이촌지구' },
  { id: 4, title: '지역 축구팀 친선 경기', date: '2025.05.18', location: '목동 운동장' },
  { id: 5, title: '등산 크루 정기 모임', date: '2025.05.25', location: '북한산' },
  { id: 6, title: '바다 러닝 마라톤 (하프)', date: '2025.06.01', location: '부산 해운대' },
  { id: 7, title: '스트릿 댄스 배틀 파티', date: '2025.06.15', location: '홍대입구' },
  { id: 8, title: '신입 크루 환영 볼링 대회', date: '2025.06.29', location: '강남 볼링장' },
];

const EventIndexContainer = () => {
  return (
    <div className="event-index-container">

      {/* 1. 길게 늘어진 헤더 섹션 */}
      <header className="event-header">
        <h1>🏃‍♂️ 현재 진행 중인 크루 이벤트</h1>
        <p>함께 즐길 수 있는 다양한 스포츠 및 친목 행사를 확인해보세요.</p><br /><br />
      </header>

      {/* 2. 공백 */}
      <div className="spacer"></div>

      {/* 3. 이벤트 그리드 목록 섹션 (한 줄에 2개씩, 총 4줄) */}
      <main className="event-grid-list">
        {events.map((event) => (
          <div key={event.id} className="event-card">

            {/* 이미지가 없는 경우 회색 화면과 "NO IMAGE" 텍스트 출력 */}
            <div className="event-image-placeholder no-image">
              <p>NO IMAGE</p>
            </div>

            <div className="event-info">
              <h2>{event.title}</h2>
              <p className="event-date">📅 {event.date}</p>
              <p className="event-location">📍 {event.location}</p>
              <button className="view-button">상세 보기</button>
            </div>
          </div>
        ))}
      </main>

    </div>
  );
};

export default EventIndexContainer