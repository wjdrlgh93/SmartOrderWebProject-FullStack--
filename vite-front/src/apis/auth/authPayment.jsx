import axios from "axios";
import { BACK_BASIC_URL } from "../commonApis";

const ACCESS_TOKEN_KEY = localStorage.getItem("accessToken");

export const authMyPaymentFn = async (memberId, currentPage, search) => {
  try {
    const res = await axios(
      `${BACK_BASIC_URL}/api/payments/myPayment/${memberId}`,
      {
        params: { page: currentPage - 1, size: 4, keyword: search },
      }
    );
    return res;
  } catch (err) {
    console.log("회원 결제페이지 조회 실패, ", err);
  }
};
