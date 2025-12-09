import "./App.css";
import { RouterProvider } from "react-router-dom";
import root from "./router/root";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { login, setInitialized } from "./slices/loginSlice";
import { setAccessToken } from "./slices/jwtSlice";
import { indexUserDetailFn } from "./apis/auth/authDetail";

function App() {
  const dispatch = useDispatch();

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    let payload;

    if (token) {
      payload = JSON.parse(atob(token.split(".")[1]));
    } else {
      console.log("토큰이 존재하지 않습니다. 로그인이 필요합니다.");
      dispatch(setInitialized(true));
      return;
    }

    const userData = async () => {
      try {
        const res = await indexUserDetailFn(token);
        const memberData = res.data;

        dispatch(
          login({
            id: payload.id || "",
            userEmail: memberData.userEmail || "",
            role: payload.role || "",
            nickName: memberData.nickName || "",
            isLogin: true,
          })
        );

        dispatch(setAccessToken(token));
        dispatch(setInitialized(true));
      } catch (err) {
        dispatch(setInitialized(true));
        console.log("새로고침 로그인 오류 발생 >> ", err);
      }
    };
    userData();
  }, []);

  // 로그인 정보가 들어와야 페이지 로드되게 해줌
  const isInitialized = useSelector((state) => state.loginSlice.isInitialized);

  if (!isInitialized) {
    return <div>로딩중...</div>;
  }

  return (
    <>
      <RouterProvider router={root} />
    </>
  );
}

export default App;
