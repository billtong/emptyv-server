package com.empty.controller.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.empty.service.impl.BaseUseServiceImpl;

@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    @Resource(name = "userService")
    BaseUseServiceImpl userService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        try {
            String token = req.getParameter("token");
            Integer userId = Integer.parseInt(req.getParameter("userId"));
            if (userId == 0) {
                res.setStatus(403);
                return false;
            }
            String sessionId = req.getParameter("sessionId");
            Boolean isTokenCorrect = userService.checkUserToken(userId, token, sessionId);
            if (isTokenCorrect) {
                return true;
            }
        } catch (Exception e) {
            res.setStatus(403);
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
