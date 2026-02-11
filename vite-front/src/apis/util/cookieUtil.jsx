import React from 'react'
import { Cookies } from 'react-cookie';

const cookies = new Cookies();

// 이름, 만료시간 넣어서 쿠키를 저장합니다.
export const setCookie = (name, value, days) => {
  const expires = new Date();
  expires.setUTCDate(expires.getUTCDate() + days); // 보관기간 (만료시간)
  return cookies.set(name, value, { path: "/", expires: expires });
}

/*
name :: 쿠키 이름
expires :: 쿠키 만료 시간
path :: 쿠키가 유효한 경로 (사이트 전체에 유효!)
*/

// 쿠키의 값을 가져옵니다.
export const getCookie = (name) => {
  console.log(cookies.get(name));
  return cookies.get(name);
}

// 쿠키를 제거합니다.
export const removeCookie = (name, path = "/") => {
  cookies.remove(name, { path })
}

