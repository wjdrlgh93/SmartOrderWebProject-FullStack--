import React from 'react'

import "../../css/common/footer.css"

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-top">
        <div className="footer-con">
          <h3>회사 정보</h3>
          <p>상호명: (주)같이달리조</p>
          <p>대표자: 홍길동</p>
          <p>주소: 서울특별시 노원구 상계로3길 21, 화일빌딩 3층</p>
          <p>사업자등록번호: 123-45-67890</p>
          <p>통신판매업신고: 제2025-서울노원-0000호</p>
          <p>이메일: sungwoo@example.com</p>
        </div>

        <div className="footer-con">
          <h3>고객센터</h3>
          <p>전화: 02-1234-5678</p>
          <p>운영시간: 평일 09:20 ~ 17:50 (점심시간 13:10 ~ 14:00)</p>
          <p>휴무: 주말 및 공휴일</p>
        </div>

        <div className="footer-con">
          <h3>바로가기</h3>
          <ul>
            <li><a href="#">이용약관</a></li>
            <li><a href="#">개인정보처리방침</a></li>
            <li><a href="#">고객문의</a></li>
            <li><a href="#">자주 묻는 질문</a></li>
          </ul>
        </div>

        <div className="footer-con">
          <h3>Follow Us</h3>
          <ul className="social">
            <li><a href="#" target="_blank" rel="noopener noreferrer">Facebook</a></li><br />
            <li><a href="#" target="_blank" rel="noopener noreferrer">Twitter</a></li><br />
            <li><a href="#" target="_blank" rel="noopener noreferrer">Instagram</a></li><br />
            <li><a href="#" target="_blank" rel="noopener noreferrer">YouTube</a></li>
          </ul>
        </div>
      </div>
      <div className="footer-bottom">
        <p>&copy; 2025 같이달리조. All rights reserved.</p>
      </div>
    </footer>
  );
};


export default Footer