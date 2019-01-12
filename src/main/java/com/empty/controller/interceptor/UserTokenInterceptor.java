package com.empty.controller.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.empty.service.impl.BaseUseServiceImpl;

public class UserTokenInterceptor implements HandlerInterceptor {

	@Resource(name = "userService")
	BaseUseServiceImpl userService;
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
			throws Exception {
		String token = req.getParameter("token");
		Integer userId = Integer.parseInt(req.getParameter("userId"));
		String sessionId = req.getParameter("sessionId");
		
		
		Boolean isTokenCorrect= userService.checkUserToken(userId, token, sessionId);
		if(isTokenCorrect) {
			return true;
		} else {
			res.setStatus(403);
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
	}
}
