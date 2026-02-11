import jwtAxios from "../util/jwtUtil"; // JWT 자동 갱신 처리된 axios

const PAYMENT_API = "http://localhost:8088/api/payments";
const PAYMENT_ITEM_API = "http://localhost:8088/api/payment-items";

// -----------------------------
// CRUD API
// -----------------------------

export const createPayment = async (paymentData) => {
  const res = await jwtAxios.post(PAYMENT_API, paymentData);
  return res.data;
};

export const getAllPayments = async () => {
  const res = await jwtAxios.get(PAYMENT_API);
  return res.data;
};

export const deletePayment = async (paymentId) => {
  await jwtAxios.delete(`${PAYMENT_API}/${paymentId}`);
};

export const getPaymentItems = async (paymentId) => {
  const res = await jwtAxios.get(`${PAYMENT_ITEM_API}/by-payment/${paymentId}`);
  return res.data;
};

// -----------------------------
// 페이징 + 검색 API
// -----------------------------
export const getPaymentsByPage = async (page = 0, size = 5, keyword = "") => {
  const res = await jwtAxios.get(`${PAYMENT_API}/page`, {
    params: {
      page,
      size,
      keyword
    }
  });
  return res.data;
};

// -----------------------------
// KakaoPay 및 현금/카드 연동 (PG 요청)
// -----------------------------

// ⭐️ [수정] pgRequest: 'kakao', 'CARD', 'CASH' 타입을 모두 허용하고 백엔드로 전달합니다.
export const pgRequest = async (pg, paymentDto) => {
    // 1. 허용되는 PG 타입 목록을 정의합니다.
    const allowedPgs = ["kakao", "CARD", "CASH"]; 

    if (!allowedPgs.includes(pg)) {
        // 'kakao', 'CARD', 'CASH'가 아닌 경우 오류 발생
        throw new Error("지원되지 않는 결제 타입입니다."); 
    }

    // 2. 모든 결제 타입은 동일한 엔드포인트로 paymentDto를 전송합니다.
    const res = await jwtAxios.post(`${PAYMENT_API}/pg/${pg}`, paymentDto, {
        headers: {
            'Content-Type': 'application/json' 
        }
    });

    // 백엔드는 카카오페이('kakao') 요청 시에만 리다이렉션 URL을 반환하고,
    // CARD/CASH 요청 시에는 성공 URL을 approvalUrl 필드에 담아 반환합니다.
    return res.data.approvalUrl; 
};

// -----------------------------
// 결제 승인 (success 페이지에서 호출)
// -----------------------------
export const approvePayment = async (paymentId, productPrice, productName, memberId, pgToken) => {
  if (!pgToken) throw new Error("pg_token이 필요합니다.");

  await jwtAxios.get(
    `${PAYMENT_API}/approval/${paymentId}/${Number(productPrice)}/${encodeURIComponent(String(productName))}/${memberId}`,
    { params: { pg_token: pgToken } }
  );
};