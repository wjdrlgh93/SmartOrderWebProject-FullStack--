import axios from "axios";
import { BACK_BASIC_URL } from "../commonApis";
import { setCookie } from "./cookieUtil";
import store from "../../store/store";
import { setAccessToken } from "../../slices/jwtSlice";
import { logoutFn } from "../auth/logout";

const jwtAxios = axios.create();
let isLoggingOut = false;


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


const beforeReq = (config) => {
  console.log("before request....");
  const accessToken = localStorage.getItem("accessToken");


  if (!accessToken) {
    console.log("Member Not Found");

    return Promise.reject({
      response: { data: { error: "REQUIRE_LOGIN" } },
    });
  }

  return config;
};


const requestFail = (err) => {
  console.log("Request error...");
  return Promise.reject(err);
};


const beforeRes = async (res) => {
  console.log("before return response....");
  return res;
};


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


jwtAxios.interceptors.request.use(beforeReq, requestFail);


jwtAxios.interceptors.response.use(beforeRes, responseFail);

export default jwtAxios;
