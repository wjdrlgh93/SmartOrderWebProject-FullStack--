import React, { useEffect, useState, useMemo } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { getCartByToken } from "../../../apis/cart/cartApi";
import { pgRequest } from "../../../apis/payment/paymentApi";
import "../../../css/payment/PaymentPage.css";


import { authDetailFn } from "../../../apis/auth/authDetail"; 


const mapCartItemsToPaymentItems = (cartItems) => {
    return cartItems.map(item => ({
        itemId: item.itemId,           
        price: item.itemPrice,         
        size: item.itemSize,           
        title: item.itemTitle,         
    }));
};


const PaymentPage = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const { checkedItems: itemsToPayIds = [] } = location.state || {};

    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);

    const [paymentType, setPaymentType] = useState("kakao"); 
    

    const [memberId, setMemberId] = useState(null); 


    const [receiverName, setReceiverName] = useState(""); 
    const [receiverPhone, setReceiverPhone] = useState(""); 
    

    const [address, setAddress] = useState("");
    const [postcode, setPostcode] = useState("");
    const [method, setMethod] = useState("배송");



    useEffect(() => {
        const fetchAllData = async () => {
            setLoading(true);
            
            try {

                const cartData = await getCartByToken();
                setCart(cartData?.items?.length ? cartData : null);


                const fetchedMemberId = cartData?.memberId;

                if (!fetchedMemberId) {
                    alert("로그인이 필요하거나 장바구니에서 회원 정보를 가져올 수 없습니다.");
                    navigate("/auth/login");
                    return;
                }

                setMemberId(fetchedMemberId); 



                const res = await authDetailFn(); 
                

                if (!res || !res.data || !res.data.userName) {

                    alert("회원 정보를 가져오는 데 실패했습니다.");
                    navigate("/auth/login");
                    return; // 리다이렉트 후 함수 종료
                }


                setReceiverName(res.data.userName || ""); 
                setReceiverPhone(res.data.phone || ""); 
                setAddress(res.data.address || "");


            } catch (e) {

                console.error("데이터 로딩 실패:", e);
                alert("데이터 로딩 중 오류가 발생했습니다. (장바구니 또는 회원 조회)");

            } finally {
                setLoading(false);
            }
        };
        fetchAllData();
    }, [navigate]); // navigate가 변경되어도 재실행되지 않도록 의존성 배열을 확인해주세요.


    const getItemsToPay = () => {
        if (!cart || !cart.items) return [];

        if (itemsToPayIds.length > 0) {
            return cart.items.filter(item =>
                itemsToPayIds.includes(item.cartItemId)
            );
        }

        return [];
    };


    const calculateTotalPrice = (items) => {
        return items.reduce(
            (sum, item) => sum + (item.itemPrice || 0) * (item.itemSize || 1),
            0
        );
    };


    const handlePayment = async () => {
        const itemsToPay = getItemsToPay();

        if (itemsToPay.length === 0) {
            alert("결제할 상품을 선택해주세요.");
            return;
        }


        if (!receiverName || !receiverPhone || !address || !postcode) {
            alert("이름, 연락처, 주소, 우편번호를 모두 입력해주세요.");
            return;
        }

        try {
            const totalPrice = calculateTotalPrice(itemsToPay);

            const currentMemberId = cart?.memberId || memberId; 
            
            if (!currentMemberId) throw new Error("회원 정보가 없습니다.");
            

            const paymentDto = {
                memberId: currentMemberId,
                paymentReceiver: receiverName, 
                paymentPhone: receiverPhone,
                
                paymentAddr: address,
                paymentPost: postcode,
                paymentMethod: method, 
                paymentType: paymentType.toUpperCase(), 


                paymentItems: mapCartItemsToPaymentItems(itemsToPay),

                productPrice: totalPrice, 

                isSucceeded: paymentType === 'kakao' ? 0 : 1, 
            };
            
            if (paymentType === "kakao") {

                const approvalUrl = await pgRequest("kakao", paymentDto);
                window.location.href = approvalUrl;
            } else {

                await pgRequest(paymentType.toUpperCase(), paymentDto); 

                alert("결제가 완료되었습니다.");
                navigate("/payment/success", { replace: true });
            }
        } catch (e) {
            console.error("결제 실패:", e);
            alert("결제 실패: " + (e.response?.data || e.message));
        }
    };

    const itemsToDisplay = getItemsToPay();
    const totalPrice = calculateTotalPrice(itemsToDisplay);

    if (loading) return <div className="loading-state">잠시만 기다려주세요...</div>;

    if (!cart || itemsToDisplay.length === 0) return (
        <div className="payment-empty">
            <p>결제할 상품이 없습니다. 장바구니에서 상품을 선택해주세요.</p>
            <button onClick={() => navigate("/cart")}>장바구니로 돌아가기</button>
        </div>
    );

    return (
        <div className="paymentPage">
            <h1>🛍️ 주문/결제</h1>

            <div className="payment-container">
                {/* 1. 결제 대상 목록 */}
                <div className="payment-section payment-items">
                    <h2>📦 주문 상품 ({itemsToDisplay.length}개)</h2>
                    <table className="payment-table">
                        <thead>
                            <tr>
                                <th>상품 정보</th>
                                <th>수량</th>
                                <th>가격</th>
                                <th>합계</th>
                            </tr>
                        </thead>
                        <tbody>
                            {itemsToDisplay.map((item) => (
                                <tr key={item.cartItemId}>
                                    <td className="item-title">
                                        <span>{item.itemTitle}</span>
                                    </td>
                                    <td>{item.itemSize || 1}</td>
                                    <td>{item.itemPrice.toLocaleString()}원</td>
                                    <td className="item-total">
                                        {(item.itemPrice * (item.itemSize || 1)).toLocaleString()}원
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* 2. 배송/결제 정보 및 최종 금액 */}
                <div className="payment-info-box">
                    
                    {/* 받는 분 정보 */}
                    <div className="payment-section receiver-info">
                        <h2>👥 받는 분 정보</h2>
                        <div className="form-group">
                            <label htmlFor="receiver-name">받는 분 이름</label>
                            <input 
                                id="receiver-name"
                                type="text" 
                                value={receiverName} 
                                onChange={(e) => setReceiverName(e.target.value)} 
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="receiver-phone">연락처</label>
                            <input 
                                id="receiver-phone"
                                type="text" 
                                value={receiverPhone} 
                                onChange={(e) => setReceiverPhone(e.target.value)} 
                            />
                        </div>
                    </div>
                    
                    {/* 배송지 정보 */}
                    <div className="payment-section shipping-info">
                        <h2>🚚 배송지 정보</h2>
                        <div className="form-group">
                            <label htmlFor="address">받는 주소</label>
                            <input 
                                id="address"
                                type="text" 
                                value={address} 
                                onChange={(e) => setAddress(e.target.value)} 
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="postcode">우편번호</label>
                            <input 
                                id="postcode"
                                type="text" 
                                value={postcode} 
                                onChange={(e) => setPostcode(e.target.value)} 
                            />
                        </div>
                    </div>

                    {/* 결제 방법 선택 */}
                    <div className="payment-section payment-method">
                        <h2>💳 결제 수단</h2>
                        <div className="form-group">
                            <label htmlFor="payment-select">결제 방법</label>
                            <select 
                                id="payment-select" 
                                value={paymentType} 
                                onChange={(e) => setPaymentType(e.target.value)}
                            >
                                <option value="kakao">카카오페이 (PG)</option>
                                <option value="CARD">신용/체크카드 (즉시 결제)</option>
                                <option value="CASH">현금 결제 (즉시 결제)</option>
                            </select>
                        </div>
                    </div>

                    {/* 최종 결제 금액 요약 */}
                    <div className="payment-summary">
                        <h3>최종 결제 금액</h3>
                        <div className="summary-row">
                            <span>상품 금액</span>
                            <span>{totalPrice.toLocaleString()}원</span>
                        </div>
                        <div className="summary-row">
                            <span>배송비</span>
                            <span>0원 (무료)</span>
                        </div>
                        <div className="summary-row total-price">
                            <strong>총 결제 금액</strong>
                            <strong>{totalPrice.toLocaleString()}원</strong>
                        </div>

                        {/* 결제 버튼 */}
                        <button 
                            onClick={handlePayment} 
                            className={`payment-button ${paymentType === 'kakao' ? 'kakao-pay-button' : 'default-button'}`}
                        >
                            {paymentType === 'kakao' ? '카카오페이로 결제하기' : '결제하기'}
                        </button>
                    </div>

                </div>
            </div>
        </div>
    );
};

export default PaymentPage;