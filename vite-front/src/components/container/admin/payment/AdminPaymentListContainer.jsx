import React, { useEffect, useState } from "react";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { useNavigate } from "react-router-dom";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";

import "../../../../css/admin/container/AdminPaymentListContainer.css";
import { useSelector } from "react-redux";
import { formatDate } from "../../../../js/formatDate";

const AdminPaymentListContainer = () => {
  const [payments, setPayments] = useState([]);
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

  const fetchData = async () => {
    const res = await jwtAxios.get(`${BACK_BASIC_URL}/api/admin/payments`, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    setTotalPages(res.data.totalPages);
    setPayments(res.data.content);
  };

  const handleChangeStatus = async (paymentId, newStatus) => {
    try {
      const res = await jwtAxios.put(`${BACK_BASIC_URL}/api/admin/payments/updateStatus/${paymentId}`,
        { status: newStatus },
        { headers: { Authorization: `Bearer ${accessToken}` } }
      );
      alert("결제 상태가 변경되었습니다");
      fetchData();
 
    } catch (error) {
      console.log("결제상태 변경 실패", error)
      alert("상태변경 실패");
    }
  }

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="admin-paymentList">
      <div className="admin-paymentList-con">

        <div className="admin-paymentList-header">
          <h2>결제 리스트</h2>
        </div>

        <div className="admin-paymentList-tableWrapper">
          <table className="admin-paymentList-table">
            <thead>
              <tr>
                <th>결제ID</th>
                <th>회원ID</th>
                <th>주문자</th>
                <th>결제방식</th>
                <th>결제시간</th>
                <th>총금액</th>
                <th>상태</th>
                <th>상세</th>
              </tr>
            </thead>
            <tbody>
              {payments.map((pay) => (
                <tr key={pay.id}>
                  <td>{pay.paymentId}</td>
                  <td>{pay.memberId}</td>
                  <td>{pay.paymentReceiver}</td>
                  <td>{pay.paymentType}</td>
                  <td>{formatDate(pay.createTime)}</td>
                  <td>{pay.productPrice.toLocaleString()}원</td>
                  <td>
                    <select
                      value={pay.paymentStatus}
                      onChange={(e) => handleChangeStatus(pay.paymentId, e.target.value)}
                    >
                      <option value="PENDING">결제대기중</option>
                      <option value="DELIVERING">배송중</option>
                      <option value="COMPLETED">배송완료</option>
                      <option value="FAILED">결제실패</option>
                      <option value="CANCELED">결제취소</option>
                      <option value="REFUNDED">환불완료</option>
                    </select>
                  </td>
                  <td>
                    <button
                      onClick={() => navigate(`/admin/paymentDetail/${pay.paymentId}`)}
                      className="view-btn"
                    >
                      보기
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="pagination">
          {[...Array(totalPages)].map((_, i) => (
            <button
              key={i}
              onClick={() => setPage(i)}
              className={i === page ? "active" : ""}
            >
              {i + 1}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AdminPaymentListContainer;
