import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { authMyPaymentFn } from "../../../apis/auth/authPayment";
import { formatDate, formattedPrice } from "../../../js/formatDate";
import { useNavigate } from "react-router";
import AdminPagingComponent from "../../common/AdminPagingComponent";
import DeliveryStatusModal from "../payment/DeliveryStatusModal";

const AuthPaymentContainer = () => {
  const paymentStatus = {
    PENDING: "결제대기",
    DELIVERING: "배송중",
    COMPLETED: "배송완료",
    FAILED: "결제실패",
    CANCELED: "결제취소",
    REFUNDED: "환불완료",
  };

  const paymentType = {
    CARD: "카드결제",
    CASH: "현금결제",
    KAKAO: "카카오페이",
  };

  const [myPayment, setMyPayment] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [search, setSearch] = useState("");
  const [defaultSearch, setDefaultSearch] = useState("");
  const [pageData, setPageData] = useState({});
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedPayment, setSelectedPayment] = useState(null);

  const memberId = useSelector((state) => state.loginSlice.id);
  const navigate = useNavigate();

  const myPagePaymentFn = async () => {
    const res = await authMyPaymentFn(memberId, currentPage, search);
    setMyPayment(res.data.content);
    setPageData(res.data);
    console.log(res);
  };

  const hadlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleOpenModal = (e, payment) => {
    e.stopPropagation();
    setSelectedPayment(payment);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedPayment(null);
    setIsModalOpen(false);
  };

  useEffect(() => {
    myPagePaymentFn();
  }, [currentPage, defaultSearch]);
  return (
    <div className="auth-my-payment">
      <div className="auth-my-payment-search">
        <h1>결제목록</h1>
        <div className="auth-my-payment-search-con">
          <input
            type="text"
            name="keyowrd"
            id="keyowrd"
            placeholder="주문한 상품을 검색할 수 있어요."
            onChange={(e) => {
              setSearch(e.target.value);
            }}
            onKeyDown={(e) => {
              if (e.key == "Enter") {
                setCurrentPage(1);
                setDefaultSearch(e.target.value);
              }
            }}
          />
          <button
            onClick={() => {
              setCurrentPage(1);
              setDefaultSearch(search);
            }}
          >
            검색
          </button>
        </div>
      </div>
      {myPayment.length <= 0 ? (
        defaultSearch != "" ? (
          <div className="my-payment-no-show">
            <div className="my-payment-no-show-con">
              <div className="my-payment-no-show-title">
                <img src="/images/myPage/boxEmpty.png" alt="" />
                <h1>'{defaultSearch}'에 해당하는 주문 내역이 없습니다. 😭</h1>
                <span>다른 키워드로 다시 검색해 보세요.</span>
                <button
                  onClick={() => {
                    navigate("/store");
                  }}
                >
                  스토어 이동
                </button>
              </div>
            </div>
          </div>
        ) : (
          <div className="my-payment-no-show">
            <div className="my-payment-no-show-con">
              <div className="my-payment-no-show-title">
                <img src="/images/myPage/boxEmpty.png" alt="빈상자" />
                <h1>💳 주문 내역이 없습니다.</h1>
                <span>
                  첫 주문을 기다리고 있습니다! 지금 바로 쇼핑을 시작해 보세요.
                </span>
                <button
                  onClick={() => {
                    navigate("/store");
                  }}
                >
                  구매하러가기
                </button>
              </div>
            </div>
          </div>
        )
      ) : (
        <div className="auth-my-payment-con">
          <div className="paymentGrid">
            {myPayment.map((payment) => {
              const items = payment.paymentItems || [];
              const totalAmount = items.reduce(
                (sum, item) => sum + (item.price || 0) * (item.size || 1),
                0
              );
              const firstItemTitle =
                items.length > 0 ? items[0].title : "상품 정보 없음";
              const status =
                paymentStatus[payment.paymentStatus] || "주문 완료"; // 상태 사용

              return (
                <div
                  className="orderCard"
                  key={payment.paymentId}
                  onClick={() => {
                    navigate(`/myPage/payment/${payment.paymentId}`);
                  }}
                >
                  <div className="top">
                    <div className="orderHeader">
                      <span className="orderId">
                        {formatDate(payment.createTime)}
                      </span>
                      <span
                        className={`deliveryStatusLink status-${status.replace(
                          /\s/g,
                          ""
                        )}`}
                        onClick={(e) => handleOpenModal(e, payment)}
                        title="클릭하여 배송 상세 조회"
                      >
                        {status}
                      </span>
                    </div>
                    <div className="mainInfo">
                      <p className="itemTitle">
                        {firstItemTitle}{" "}
                        {items.length > 1 ? `외 ${items.length - 1}개` : ""}
                      </p>
                      <p className="totalAmount">
                        총 {totalAmount.toLocaleString()}원
                      </p>
                    </div>
                    <ul className="orderDetailList">
                      <li>주문처: {payment.paymentPost}</li>
                      <li>배송주소: {payment.paymentAddr}</li>
                      <li>결제방법: {paymentType[payment.paymentType]}</li>
                    </ul>
                  </div>
                  <div className="bottom">
                    <h4>주문 상세</h4>
                    <ul className="payment_itemList">
                      {items.map((item, index) => (
                        <li key={item.id || index}>
                          <span>{item.title || "-"}</span>
                          <div className="item-price-size">
                            <span className="itemPrice">
                              {(item.price || 0).toLocaleString()}원
                            </span>
                            <span className="itemSize">{item.size || 0}개</span>
                          </div>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>
              );
            })}
          </div>
          <AdminPagingComponent
            pageData={pageData}
            onPageChange={hadlePageChange}
          />
        </div>
      )}
      {/* 모달 컴포넌트 렌더링 */}
      {isModalOpen && selectedPayment && (
        <DeliveryStatusModal
          payment={selectedPayment}
          onClose={handleCloseModal}
        />
      )}
    </div>
  );
};

export default AuthPaymentContainer;
