import axios from "axios";
import { BACK_BASIC_URL } from "../commonApis";
import { setCookie } from "./cookieUtil";
import store from "../../store/store";
import { setAccessToken } from "../../slices/jwtSlice";
import { logoutFn } from "../auth/logout";

const jwtAxios = axios.create();
let isLoggingOut = false;

// ----------- 리프레시 토큰을 불러와서 액세스 토큰 갱신 -----------
const refreshTokenFn = async () => {
  try {
    const res = await axios.post(
      `${BACK_BASIC_URL}/api/refresh/token`,
      {},
      { withCredentials: true }
    );
    const accessToken = res.data.accessToken;
    store.dispatch(setAccessToken(accessToken));
    localStorage.setItem("accessToken", accessToken);
    return res;
  } catch (err) {
    if (!isLoggingOut) {
      isLoggingOut = true;
      logoutFn();
      alert("재로그인이 필요합니다.");
      window.location.href = "/auth/login/";
    }
    console.log("어떤 오류임? ", err);
  }
};

// ----------- before request 요청 인터셉터 -----------
const beforeReq = (config) => {
  console.log("before request....");
  const accessToken = localStorage.getItem("accessToken");

  // header에 access 토큰이 없으면 로그인 안되어있는걸로 간주
  if (!accessToken) {
    console.log("Member Not Found");

    return Promise.reject({
      response: { data: { error: "REQUIRE_LOGIN" } },
    });
  }

  return config;
};

// ----------- fail request 요청 실패 인터셉터 -----------
const requestFail = (err) => {
  console.log("Request error...");
  return Promise.reject(err);
};

// ----------- before return response 응답 인터셉터 -----------
const beforeRes = async (res) => {
  console.log("before return response....");
  return res;
};

// ----------- response fail -----------
const responseFail = async (err) => {
  console.log("response fail error....");

  if (err.response.status === 401 && err.response.data === "TOKEN_EXPIRED") {
    console.log("토큰이 만료되어 재발급 받습니다.");
    let rs;

    try {
      rs = await refreshTokenFn();
    } catch (err) {
      console.log("리프레시 토큰 갱신 실패 ", +err);
      if (!isLoggingOut) {
        isLoggingOut = true;
        logoutFn();
        alert("재로그인이 필요합니다.");
        window.location.href = "/auth/login/";
      }
      return Promise.reject(err);
    }

    const originalRequest = err.config;
    originalRequest.headers.Authorization = `Bearer ${rs.data.accessToken}`;

    return await jwtAxios(originalRequest);
  }

  return Promise.reject(err);
};

// 요청 인터셉터(beforeReq): 요청을 보내기 전에 엑세스 토큰을 Authorization 헤더에 추가
jwtAxios.interceptors.request.use(beforeReq, requestFail);

// 응답 인터셉터(beforeReq): 응답이 오면 엑세스 토큰이 만료되었는지 확인하고, 만료된 경우 자동으로 토큰을 갱신하여 원래의 요청을 재시도
jwtAxios.interceptors.response.use(beforeRes, responseFail);

export default jwtAxios;
