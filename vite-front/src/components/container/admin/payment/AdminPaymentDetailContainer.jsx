import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import { useSelector } from "react-redux";

import "../../../../css/admin/container/AdminPaymentDetailContainer.css";
import { formatDate } from "../../../../js/formatDate";

const AdminPaymentDetailContainer = () => {
  const { paymentId } = useParams();

  const navigate = useNavigate();

  const accessToken = useSelector((state) => state.jwtSlice.accessToken);

  const [payment, setPayment] = useState(null);

  const statusMap = {
    PENDING: "결제대기",
    DELIVERING: "배송중",
    COMPLETED: "배송완료",
    FAILED: "결제실패",
    CANCELED: "결제취소",
    REFUNDED: "환불완료",
  };

  const fetchDetail = async () => {
    try {
      const res = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/payments/${paymentId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );
      setPayment(res.data);
      console.log(res.data);
    } catch (error) {
      console.log("결제상세페이지 조회 실패", error),
        alert("결제 상세정보 조회 실패");
    }
  };

  useEffect(() => {
    fetchDetail();
  }, [paymentId]);

  if (!payment) return <p>로딩중...</p>;

  return (
    <div className="admin-payment-detail">
      <h2>결제 상세 정보</h2>
      <div className="payment-info-box">
        <p>
          <strong>결제ID : </strong> {payment.paymentId}
        </p>
        {payment.memberStatus != null ? (
          <p>
            <strong>회원ID : </strong> {payment.memberId} (
            {payment.memberStatus})
          </p>
        ) : (
          <p>
            <strong>회원ID : </strong> {payment.memberId}
          </p>
        )}
        <p>
          <strong>받는사람 : </strong> {payment.paymentReceiver}
        </p>
        <p>
          <strong>주소 : </strong> {payment.paymentAddr}
        </p>
        <p>
          <strong>결제방식 : </strong> {payment.paymentType}
        </p>
        <p>
          <strong>결제상태 : </strong> {statusMap[payment.paymentStatus]}
        </p>
        <p>
          <strong>결제시간 : </strong> {formatDate(payment.createTime)}
        </p>
        <p>
          {" "}
          <strong>
            총 금액 :
          </strong> {payment.productPrice?.toLocaleString()} 원{" "}
        </p>
      </div>
      <h3>결제 상품 목록</h3>
      <table className="payment-item-table">
        <thead>
          <tr>
            <th>상품명</th>
            <th>가격</th>
            <th>수량</th>
            <th>합계</th>
          </tr>
        </thead>
        <tbody>
          {" "}
          {payment.paymentItems?.map((item, index) => (
            <tr key={index}>
              <td>{item.title}</td>
              <td>{item.price.toLocaleString()} 원</td>
              <td>{item.size}</td>
              <td>{(item.price * item.size).toLocaleString()} 원</td>
            </tr>
          ))}{" "}
        </tbody>
      </table>
      <button
        onClick={() => navigate("/admin/paymentList")}
        className="back-btn"
      >
        목록으로 돌아가기{" "}
      </button>
    </div>
  );
};

export default AdminPaymentDetailContainer;
