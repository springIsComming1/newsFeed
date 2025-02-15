package com.example.newsfeed.common.filter;

import com.example.newsfeed.common.consts.Const;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {"/users/save", "/logins"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

            if(!isWhiteList(requestURI)){
                HttpSession session = request.getSession(false);

                if (session == null || session.getAttribute(Const.LOGIN_USER) == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 해주세요");
                    return;
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
