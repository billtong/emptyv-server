package com.empty.controller;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empty.entity.UserEntity;
import com.empty.service.impl.BaseUseServiceImpl;

@Controller
@RequestMapping(value = "/api/user", produces = "application/json;charset=UTF-8")
public class UserController {

	@Resource(name = "userService")
	BaseUseServiceImpl userService;

	/**
	 * 注册endpoint
	 * 1.Body里要至少有userName userPassword userEmail
	 * @param signMap
	 * @return
	 */
	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, String> userSignUp(HttpServletResponse res, @RequestBody HashMap<String, String> signMap) {
		
		HashMap<String, String> message = new HashMap<>();
		
		UserEntity newUser = new UserEntity();
		newUser.setUserName(signMap.get("userName"));
		newUser.setUserPassword(signMap.get("userPassword"));
		newUser.setUserEmail(signMap.get("userEmail"));
		
		userService.registerNewUser(newUser, message);

		return message;
	}
	
	/**
	 * 激活邮件里的链接
	 * 1.Param里要有code
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/activated",  method = RequestMethod.GET)
	public @ResponseBody String activatedNewUser(@RequestParam String code) {
		if(userService.updateUserActivateState(code)) {
			return "success";
		} else {
			return "failed";	
		}
	}
	
	/**
	 * 登陆
	 * 1.Body里要有 userName，userPassword，userEmail
	 * 返回session id，用来匹配到相应的sessionid
	 * @param session
	 * @param loginMap
	 * @return
	 */
	@RequestMapping(value = "/login",  method = RequestMethod.POST)
	public @ResponseBody HashMap<String, String> userLogin(HttpSession session, HttpServletRequest req, HttpServletResponse res, @RequestBody HashMap<String, String> loginMap) {
		HashMap<String, String> message = new HashMap<>();
		
		//将token按照用户名字储存到服务器的session中，再将该token返回
		if(userService.checkUserPassword(loginMap.get("userName"), loginMap.get("userPassword"))) {
			String token = UUID.randomUUID().toString().replace("-", "");
			session.setAttribute(loginMap.get("userName"), token);
			message.put("token", token);
			message.put("sessionId", session.getId());
			return message;
		}
		
		//其他情況是返回错误信息
		res.setStatus(401);
		message.put("message", "error");
		return message;
	}
	
	/**
	 * 加载用户信息（被token check拦截）
	 * 1.Header里要有token
	 * 2.Param里要有userId
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/getUser", method=RequestMethod.GET)
	public @ResponseBody UserEntity getAllUserInfo(@RequestParam Integer userId) {
		return userService.getUserAll(userId);
	}
	

}
