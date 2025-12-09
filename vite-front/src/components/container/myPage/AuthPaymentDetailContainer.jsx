import axios from "axios";
import React, { useEffect, useState } from "react";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import { useNavigate, useParams } from "react-router";
import { formatDate, formattedPrice } from "../../../js/formatDate";

const AuthPaymentDetailContainer = () => {
  const paymentType = {
    CARD: "카드결제",
    CASH: "현금결제",
    KAKAO: "카카오페이",
  };

  const { paymentId } = useParams();
  console.log(paymentId);
  const [paymentDetail, setPaymentDetail] = useState({});
  const [items, setItems] = useState([]);

  const navigate = useNavigate();

  const paymentDetailFn = async () => {
    try {
      const res = await axios.get(
        `${BACK_BASIC_URL}/api/payments/${paymentId}`
      );
      console.log(res.data.paymentItems);
      setPaymentDetail(res.data);
      setItems(res.data.paymentItems);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    paymentDetailFn();
  }, []);
  return (
    <>
      <div className="payment-detail-container">
        <div className="payment-detail-header">
          <h1>결제 상세 정보</h1>
          <button
            className="back-btn"
            onClick={() => {
              navigate("/myPage/payment");
            }}
          >
            ← 돌아가기
          </button>
        </div>

        <div className="payment-detail-content">
          {/* 배송지 정보 */}
          <div className="section delivery-section">
            <h2 className="section-title">배송지 정보</h2>
            <div className="info-grid">
              <div className="info-item full-width">
                <span className="info-label">배송지 주소</span>
                <span className="info-value">{paymentDetail.paymentAddr}</span>
              </div>
              <div className="info-item">
                <span className="info-label">받는사람</span>
                <span className="info-value">
                  {paymentDetail.paymentReceiver}
                </span>
              </div>
              <div className="info-item">
                <span className="info-label">연락처</span>
                <span className="info-value">{paymentDetail.paymentPhone}</span>
              </div>
            </div>
          </div>

          {/* 주문 상품 */}
          <div className="section products-section">
            <h2 className="section-title">주문 상품</h2>
            <div className="product-list">
              {items.map((el) => {
                return (
                  <div
                    key={el.id}
                    className="product-item"
                    onClick={() => {
                      navigate(`/store/detail/${el.itemId}`);
                    }}
                  >
                    <div className="product-image">
                      {el.s3file ? (
                        <img
                          src={el.s3file}
                          alt={el.title + " 이미지"}
                          classNameName="img-loaded"
                        />
                      ) : (
                        <div className="no-image">No Image</div>
                      )}
                    </div>
                    <div className="product-info">
                      <h3 className="product-title">{el.title}</h3>
                      <p className="product-detail">수량: {el.size}개</p>
                      <p className="product-price">
                        {formattedPrice(el.price)}원
                      </p>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* 결제 정보 */}
          <div className="section payment-info-section sticky-section">
            <h2 className="section-title">결제 정보</h2>
            <div className="info-list">
              <div className="info-item">
                <span className="info-label">결제일시</span>
                <span className="info-value">
                  {formatDate(paymentDetail.createTime)}
                </span>
              </div>
              <div className="info-item">
                <span className="info-label">주문번호</span>
                <span className="info-value">{paymentDetail.paymentId}</span>
              </div>
              <div className="info-item">
                <span className="info-label">결제방식</span>
                <span className="info-value">
                  {paymentType[paymentDetail.paymentType] ||
                    paymentDetail.paymentType}
                </span>
              </div>
              <div className="info-item highlight">
                <span className="info-label">결제금액</span>
                <span className="info-value price">
                  {formattedPrice(paymentDetail.productPrice)}원
                </span>
              </div>
            </div>
          </div>
          {/* 총 결제 금액 */}
          <div className="section total-section sticky-section">
            <h2 className="section-title">결제 금액</h2>
            <div className="total-info">
              <div className="total-row">
                <span>상품 금액</span>
                <span>{formattedPrice(paymentDetail.productPrice)}원</span>
              </div>
              <div className="total-row">
                <span>배송비</span>
                <span>무료</span>
              </div>
              <div className="divider"></div>
              <div className="total-row final">
                <span>총 결제 금액</span>
                <span className="final-price">
                  {formattedPrice(paymentDetail.productPrice)}원
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default AuthPaymentDetailContainer;
