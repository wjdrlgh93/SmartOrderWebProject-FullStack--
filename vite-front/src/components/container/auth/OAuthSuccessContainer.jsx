import React, { useEffect, useState } from 'react'
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router'
import { login } from '../../../slices/loginSlice';
import { setCookie } from '../../../apis/util/cookieUtil';
import { setAccessToken } from '../../../slices/jwtSlice';

const OAuthSuccessContainer = () => {

  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");

    if (token) {
      localStorage.setItem("accessToken", token);

      const payload = JSON.parse(atob(token.split('.')[1]));

      const id = payload.id;
      const userEmail = payload.userEmail;
      const role = payload.role;
      const access = token;
      const nickName = "";

      dispatch(login({ userEmail, id, role, nickName, isLogin: true }));
      dispatch(setAccessToken(access));   
      navigate("/store/index");
    }
  }, [])
  return (
    <div>로그인 중...</div>
  )
}

export default OAuthSuccessContainer