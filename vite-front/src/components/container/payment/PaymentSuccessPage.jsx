import React from "react";
import { useNavigate } from "react-router-dom";
import "../../../css/payment/PaymentPage.css";
import { useDispatch } from "react-redux";
import { removeCart } from "../../../slices/CartSlice";

const PaymentSuccessPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const goToPaymentList = () => {
    navigate("/myPage/payment");
  };

  dispatch(removeCart());

  return (
    <div className="paymentSuccess">
      <h2>🎉 결제 성공!</h2>
      <p>결제가 정상적으로 완료되었습니다.</p>
      <button className="btnPaymentList" onClick={goToPaymentList}>
        결제 내역 확인
      </button>
    </div>
  );
};

export default PaymentSuccessPage;
