import axios from "axios";
import { deleteAccessToken } from "../../slices/jwtSlice";
import { logoutAction } from "../../slices/loginSlice";
import store from "../../store/store";
import { BACK_BASIC_URL } from "../commonApis";

export const logoutFn = async () => {
  const accessToken = store.getState().jwtSlice.accessToken;
  const ACCESS_TOKEN_KEY = "accessToken";

  localStorage.removeItem(ACCESS_TOKEN_KEY);
  store.dispatch(logoutAction());
  store.dispatch(deleteAccessToken());

  try {
    const res = await axios.post(
      `${BACK_BASIC_URL}/api/member/logout`,
      {},
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
        withCredentials: true,
      }
    );
    return res.status;
  } catch (err) {
    console.log("로그아웃 처리 중 오류 발생:", err);
  }
};
