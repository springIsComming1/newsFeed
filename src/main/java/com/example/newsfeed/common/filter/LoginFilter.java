package com.example.newsfeed.common.filter;

import com.example.newsfeed.common.consts.Const;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class LoginFilter implements Filter {

    private static final String[] WHITE_LIST = {"/users/save", "/logins"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        try {
            if(!isWhiteList(requestURI)){
                HttpSession session = request.getSession(false);

                if (session == null || session.getAttribute(Const.LOGIN_USER) == null) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 해주세요");
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ResponseStatusException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json; charset=UTF-8");
//            response.setCharacterEncoding("UTF-8");
//
//            response.getWriter().write();
        }
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
