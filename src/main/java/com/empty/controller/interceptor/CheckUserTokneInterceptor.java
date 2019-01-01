package com.empty.controller.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.empty.entity.UserEntity;
import com.empty.service.impl.BaseUseServiceImpl;

public class CheckUserTokneInterceptor implements HandlerInterceptor {

	@Resource(name = "userService")
	BaseUseServiceImpl userService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	//检查token session中token是否正确
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object arg2) throws Exception {
		String token = req.getHeader("token");
		UserEntity user = userService.getUserAll(Integer.valueOf(req.getParameter("userId")));
		HttpSession session = req.getSession();
		if(session.getAttribute("userName-"+user.getUserName()).equals(token)) {
			return true;
		}
		res.setStatus(403);
		System.out.println("拦截成功了！！！");
		return false;
	}

}
