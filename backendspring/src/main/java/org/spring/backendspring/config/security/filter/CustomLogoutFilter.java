package org.spring.backendspring.config.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendspring.member.dto.MemberDto;
import org.spring.backendspring.member.service.RefreshService;
import org.spring.backendspring.config.security.util.CookieUtil;
import org.springframework.web.filter.GenericFilterBean;

@Log4j2
@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final RefreshService refreshService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!request.getRequestURI().equals("/api/member/logout") ||
                !request.getMethod().equals("POST")) {
            chain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("refreshToken".equals(c.getName())) {
                    refresh = c.getValue();
                    break;
                }
            }
        }

        MemberDto memberDto = refreshService.validateRefreshTokenAndGetMember(refresh);

        refreshService.deleteRefreshToken(memberDto.getUserEmail());
        Cookie deleteCookie = CookieUtil.deleteCookie("refreshToken");
        response.addCookie(deleteCookie);
        response.setStatus(HttpServletResponse.SC_OK);

        log.info("---------------------------------------");
        log.info("로그아웃 성공");
        log.info("---------------------------------------");
    }
}
