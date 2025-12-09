import store from "../../store/store";
import { BACK_BASIC_URL } from "../commonApis";
import jwtAxios from "../util/jwtUtil";

const accessToken = () => store.getState().jwtSlice.accessToken;

export const adminFn = {

  getSummary: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/summary`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  },

  getTotalMembers: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/member/total`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  },

  getTodayMembers: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/member/today`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  },

  getTotalCrews: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/crew/total`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  },

  getTodayCrews: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/crew/today`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  },

  getTotalPayments: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/payment/total`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  },

  getTodayPayments: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/payment/today`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  },

  getTotalSales: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/payment/totalSales`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    })
  },

  getTodaySales: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/payment/todaySales`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    })
  },

  getTotalBoards: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/board/total`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  },

  getTodayBoards: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/board/today`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  },

  getWeeklySales: async () => {
    return jwtAxios.get(`${BACK_BASIC_URL}/api/admin/payments/weeklySales`, {
      headers: { Authorization: `Bearer ${accessToken()}` }
    });
  }
}
