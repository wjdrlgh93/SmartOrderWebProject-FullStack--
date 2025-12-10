package org.spring.backendspring.config.security.util;

import jakarta.servlet.http.Cookie;


public class CookieUtil {


    public static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setSecure(true);     // 보안 정책 http, https
        cookie.setAttribute("SameSite", "None");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }


    public static Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 즉시 삭제
        return cookie;
    }
}
