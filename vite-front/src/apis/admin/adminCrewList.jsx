import store from "../../store/store";
import { BACK_BASIC_URL } from "../commonApis";
import jwtAxios from "../util/jwtUtil";

const accessToken = store.getState().jwtSlice.accessToken;

export const newCrewCreateRequestFn = async (currentPage, search) => {
  try {
    const res = await jwtAxios.get(
      `${BACK_BASIC_URL}/api/admin/crew/create/requestList`,
      {
        headers: { Authorization: `Bearer ${accessToken}` },
        params: { page: currentPage - 1, size: 10, keyword: search },
        withCredentials: true,
      }
    );
    return res;
  } catch (err) {
    console.log("크루 신청 불러오기 실패, ", err);
  }
};

export const adminCrewListFn = async (currentPage, search) => {
  try {
    const res = await jwtAxios.get(
      `${BACK_BASIC_URL}/api/admin/crew/crewList`,
      {
        headers: { Authorization: `Bearer ${accessToken}` },
        params: { page: currentPage - 1, size: 10, keyword: search },
        withCredentials: true,
      }
    );
    return res;
  } catch (err) {
    console.log("크루 목록 조회를 실패했습니다. " + err);
  }
};
