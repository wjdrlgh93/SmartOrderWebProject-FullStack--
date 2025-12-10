import jwtAxios from "../util/jwtUtil"; 

const PAYMENT_API = "http://localhost:8088/api/payments";
const PAYMENT_ITEM_API = "http://localhost:8088/api/payment-items";


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


export const pgRequest = async (pg, paymentDto) => {

    const allowedPgs = ["kakao", "CARD", "CASH"]; 

    if (!allowedPgs.includes(pg)) {

        throw new Error("지원되지 않는 결제 타입입니다."); 
    }


    const res = await jwtAxios.post(`${PAYMENT_API}/pg/${pg}`, paymentDto, {
        headers: {
            'Content-Type': 'application/json' 
        }
    });


    return res.data.approvalUrl; 
};


export const approvePayment = async (paymentId, productPrice, productName, memberId, pgToken) => {
  if (!pgToken) throw new Error("pg_token이 필요합니다.");

  await jwtAxios.get(
    `${PAYMENT_API}/approval/${paymentId}/${Number(productPrice)}/${encodeURIComponent(String(productName))}/${memberId}`,
    { params: { pg_token: pgToken } }
  );
};