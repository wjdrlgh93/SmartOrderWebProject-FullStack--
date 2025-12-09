import { createSlice } from "@reduxjs/toolkit";
import React, { act } from "react";

const initState = {
  id: "",
  userEmail: "",
  role: "",
  nickName: "",
  isLogin: false,
  isInitialized: false,
};

// setInitialized -> (새로고침 후 로그인 정보가 들어와야 페이지를 로드해줍니다.)

const loginSlice = createSlice({
  name: "LoginSlice",
  initialState: initState,
  reducers: {
    login: (state, action) => {
      console.log("login....");
      const data = action.payload;

      console.log(data);
      return {
        ...state,
        id: data.id,
        userEmail: data.userEmail,
        role: data.role,
        nickName: data.nickName,
        isLogin: data.isLogin,
      };
    },
    logoutAction: (state, action) => {
      console.log("logout...");
      state.isLogin = false;
      state.userEmail = "";
      nickName: "", (state.role = ""), (state.id = "");
    },
    setInitialized: (state, action) => {
      state.isInitialized = action.payload;
    },
  },
});

export const { login, logoutAction, setInitialized } = loginSlice.actions;
export default loginSlice.reducer;
