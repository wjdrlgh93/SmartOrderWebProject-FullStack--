import React, { useEffect, useState } from "react";
import { getPaymentsByPage } from "../../../apis/payment/paymentApi";
import "../../../css/payment/PaymentPage.css"; 
import DeliveryStatusModal from "./DeliveryStatusModal"; 

const PaymentListPage = () => {
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({});
  const [keyword, setKeyword] = useState("");
  const pageSize = 8;
  
  //모달 상태 추가
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedPayment, setSelectedPayment] = useState(null); 

  //임시 배송 상태 데이터 (실제 데이터에 'deliveryStatus' 필드가 없다고 가정)
  const mockDeliveryStatuses = ["배송 완료", "운송 중", "출고 준비", "주문 완료", "배송 완료", "운송 중", "출고 준비", "주문 완료"];
  
  //모달 열기/닫기 핸들러
  const handleOpenModal = (payment) => {
    setSelectedPayment(payment);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedPayment(null);
    setIsModalOpen(false);
  };


  const fetchPayments = async (page = currentPage) => {
    setLoading(true);
    try {
      const data = await getPaymentsByPage(page, pageSize, keyword);
      console.log("결제 조회 데이터:", data);

      //서버 데이터에 임시 배송 상태 추가
      const processedPayments = (data.content || []).map((payment, index) => ({
        ...payment,
        // 실제 백엔드 필드로 대체해야 합니다.
        deliveryStatus: mockDeliveryStatuses[index % mockDeliveryStatuses.length], 
      }));

      setPayments(processedPayments);
      setPageInfo({
        totalPages: data.totalPages || 1,
        hasNext: data.hasNext || false,
        hasPrevious: data.hasPrevious || false,
      });
    } catch (e) {
      console.error("결제 내역 조회 실패:", e);
      setPayments([]);
      setPageInfo({});
    } finally {
      setLoading(false);
    }
  };

  // 페이지 변경이나 초기 로딩 시 호출
  useEffect(() => {
    fetchPayments();
  }, [currentPage]);

  // 검색 제출
  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(0); // 검색 시 첫 페이지로 이동
    fetchPayments(0);
  };

  const handlePrev = () =>
    pageInfo.hasPrevious && setCurrentPage((prev) => prev - 1);
  const handleNext = () =>
    pageInfo.hasNext && setCurrentPage((prev) => prev + 1);

  if (loading) return <p className="loadingMessage">로딩 중...</p>;
  if (!payments.length && !keyword) return <p className="emptyMessage">결제 내역이 없습니다.</p>;
  if (!payments.length && keyword) return <p className="emptyMessage">'{keyword}'에 대한 검색 결과가 없습니다.</p>;


  return (
    <div className="paymentListPage">
      <h1>주문목록</h1>

      {/* 검색 */}
      <form className="paymentSearch" onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="결제 방법 또는 주문처 검색"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button type="submit">🔍 검색</button>
      </form>

      {/* 결제 목록 - 그리드 */}
      <div className="paymentGrid">
        {payments.map((payment) => {
          const items = payment.paymentItems || [];
          const totalAmount = items.reduce(
            (sum, item) => sum + (item.price || 0) * (item.size || 1),
            0
          );
          const firstItemTitle = items.length > 0 ? items[0].title : "상품 정보 없음";
          const status = payment.deliveryStatus || "주문 완료"; // 상태 사용

          return (
            <div className="orderCard" key={payment.paymentId}>
              <div className="top">
                <div className="orderHeader">
                    <span className="orderId">주문번호: {payment.paymentId}</span>
                    <span 
                        className={`deliveryStatusLink status-${status.replace(/\s/g, "")}`}
                        onClick={() => handleOpenModal(payment)} 
                        title="클릭하여 배송 상세 조회"
                    >
                        {status}
                    </span>
                </div>
                <div className="mainInfo">
                    <p className="itemTitle">{firstItemTitle} {items.length > 1 ? `외 ${items.length - 1}개` : ""}</p>
                    <p className="totalAmount">총 {totalAmount.toLocaleString()}원</p>
                </div>
                <ul className="orderDetailList">
                    <li>주문처: {payment.paymentPost}</li>
                    <li>배송주소: {payment.paymentAddr}</li>
                    <li>결제방법: {payment.paymentType}</li>
                </ul>
              </div>
              <div className="bottom">
                <h4>주문 상세</h4>
                <ul className="payment_itemList">
                    {items.map((item, index) => (
                        // item.paymentItemId 대신 index 사용 (안정적인 key가 있다면 사용 권장)
                        <li key={item.id || index}> 
                            <span>{item.title || "-"}</span>
                            <span className="itemPrice">{(item.price || 0).toLocaleString()}원</span>
                            <span className="itemSize">{item.size || 0}개</span>
                        </li>
                    ))}
                </ul>
              </div>
            </div>
          );
        })}
      </div>

      {/* 페이징 */}
      <div className="pagination">
        <button onClick={handlePrev} disabled={!pageInfo.hasPrevious}>
          &lt; 이전
        </button>
        <span>
          페이지 {currentPage + 1} / {pageInfo.totalPages || 1}
        </span>
        <button onClick={handleNext} disabled={!pageInfo.hasNext}>
          다음 &gt;
        </button>
      </div>
      
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

export default PaymentListPage;