import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { adminFn } from '../../../apis/admin/adminIndex';
import { useNavigate } from 'react-router-dom';

import "../../../css/admin/container/AdminIndexContainer.css"
import { formattedPrice } from '../../../js/formatDate'
import SalesWeeklyChart from "./payment/SalesWeeklyChart";

const AdminIndexContainer = () => {
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const [summary, setSummary] = useState({
    totalMembers: 0,
    todayMembers: 0,
    totalCrews: 0,
    todayCrews: 0,
    totalPayments: 0,
    todayPayments: 0,
    totalSales: 0,
    todaySales: 0,
    totalBoards: 0
  });

  const [member, setMember] = useState({
    totalMembers: 0,
    todayMembers: 0
  });

  const [crew, setCrew] = useState({
    totalCrews: 0,
    todayCrews: 0
  });

  const [payment, setPayment] = useState({
    totalPayments: 0,
    todayPayments: 0,
    totalSales: 0,
    todaySales: 0
  });

  const [board, setBoard] = useState({
    totalBoards: 0,
    todayBoards: 0
  });

  const [weeklySales, setWeeklySales] = useState(null);

  const fetchWeeklySales = async () => {
    try {
      const res = await adminFn.getWeeklySales(accessToken);
      setWeeklySales(res.data);
    } catch (err) {
      console.log("주간 매출 조회 실패", err);
    }
  };

  const fetchSummary = async () => {
    try {
      const res = await adminFn.getSummary(accessToken);

      setSummary(res.data);
      setMember({
        totalMembers: res.data.totalMembers,
        todayMembers: res.data.todayMembers
      });
      setCrew({
        totalCrews: res.data.totalCrews,
        todayCrews: res.data.todayCrews
      });
      setPayment({
        totalPayments: res.data.totalPayments,
        todayPayments: res.data.todayPayments,
        totalSales: res.data.totalSales,
        todaySales: res.data.todaySales
      });
      setBoard({
        totalBoards: res.data.totalBoards,
        todayBoards: res.data.todayBoards
      });
    } catch (err) {
      console.log("관리자 요약 조회 실패:", err);
    }
  };

  useEffect(() => {
    if (!accessToken) {
      navigate("/login");
      return;
    }
    fetchSummary();
    fetchWeeklySales();
  }, [accessToken]);

  return (
    <div className="indexCon">

      {/* SUMMARY */}
      <div className="summaryCard">
        <h3>SIMPLE SUMMARY</h3>
        <ul>
          <li>총 회원수: {summary.totalMembers}</li>
          <li>오늘 가입자수: {summary.todayMembers}</li>
          <li>총 크루수: {summary.totalCrews}</li>
          <li>오늘 생성크루수: {summary.todayCrews}</li>
          <li>총 결제건: {summary.totalPayments}</li>
          <li>오늘 결제건: {summary.todayPayments}</li>
          <li>총 게시글 수: {summary.totalBoards}</li>
        </ul>
      </div>

      {/* CHART */}
      <div className="chartWrapper">
        {weeklySales && <SalesWeeklyChart weeklySales={weeklySales} />}
      </div>

      {/* 아래 4개 카드 그리드 */}
      <div className="bottomGrid">
        <div className="cardBlock memberCard">
          <h3>MEMBER</h3>
          <ul>
            <li>총 회원: {member.totalMembers}</li>
            <li>오늘 가입한 회원: {member.todayMembers}</li>
          </ul>
        </div>

        <div className="cardBlock crewCard">
          <h3>CREW</h3>
          <ul>
            <li>총 크루: {crew.totalCrews}</li>
            <li>오늘 가입한 크루: {crew.todayCrews}</li>
          </ul>
        </div>

        <div className="cardBlock paymentCard">
          <h3>PAYMENT</h3>
          <ul>
            <li>총 결제건: {payment.totalPayments}</li>
            <li>오늘 결제건: {payment.todayPayments}</li>
            <li>총 매출액: {formattedPrice(payment.totalSales)} 원</li>
            <li>일간 매출액: {formattedPrice(payment.todaySales)} 원</li>
          </ul>
        </div>

        <div className="cardBlock boardCard">
          <h3>BOARD</h3>
          <ul>
            <li>총 게시물: {board.totalBoards}</li>
            <li>오늘 게시물: {board.todayBoards}</li>
          </ul>
        </div>
      </div>

    </div>
  );
};

export default AdminIndexContainer;
