package com.empty.controller;

import java.util.HashMap;
import java.util.Map;
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
import com.empty.util.MySessionContext;

@Controller
@RequestMapping(value = "/api/user", produces = "application/json;charset=UTF-8")
public class UserController {

	@Resource(name = "userService")
	BaseUseServiceImpl userService;

	// 注册endpoint 1.Body里要至少有userName userPassword userEmail
	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, String> userSignUp(HttpServletResponse res,
			@RequestBody HashMap<String, String> signMap) {
		HashMap<String, String> message = new HashMap<>();
		UserEntity newUser = new UserEntity();
		newUser.setUserName(signMap.get("userName"));
		newUser.setUserPassword(signMap.get("userPassword"));
		newUser.setUserEmail(signMap.get("userEmail"));
		userService.registerNewUser(newUser, message);
		return message;
	}

	// 登陆
	// 1.Body里要有 userName，userPassword，userEmail 返回session id，用来匹配到相应的sessionid
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> userLogin(HttpSession session, HttpServletRequest req,
			HttpServletResponse res, @RequestBody HashMap<String, String> loginMap) {
		HashMap<String, Object> message = new HashMap<>();
		// 将token按照用户名字储存到服务器的session中，再将该token返回
		if (userService.checkUserPassword(loginMap.get("userName"), loginMap.get("userPassword"))) {
			String token = UUID.randomUUID().toString().replace("-", "");
			session.setMaxInactiveInterval(7*24*60*60); //token保存的时间，即7日不上站就自己删掉了 7day=7*24*60*60	
			session.setAttribute(loginMap.get("userName"), token);
			message.put("token", token);
			message.put("sessionId", session.getId());
			message.put("user", (userService.getUserByName(loginMap.get("userName"))));
			return message;
		}
		// 其他情況是返回错误信息
		res.setStatus(401);
		message.put("message", "error");
		return message;
	}

	// 登出，将sessionContext中储存的session删掉
	// 目前没有考虑一个特殊情况，服务重启的话，可能倒是不同步而出现bug
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> userLogout(@RequestBody Map<String, String> userJson,
			HttpServletResponse res) {
		Map<String, String> message = new HashMap<>();
		try {
			// System.out.println(MySessionContext.getInstance().sessionMap.toString());
			HttpSession session = MySessionContext.getInstance().getSession(userJson.get("sessionId"));
			String serverToken = (String) session.getAttribute(userJson.get("userName"));
			String userToken = userJson.get("token");
			if (serverToken.equals(userToken)) {
				MySessionContext.getInstance().delSession(session);
				message.put("message", "delete token success");
			} else {
				message.put("message", "delete token failed");
				res.setStatus(302);
			}
		} catch (NullPointerException e) {
			message.put("message", "it is already deleted");
		}
		return message;
	}
	
	//更新用户信息
	@RequestMapping(value="update", method = RequestMethod.PATCH)
	public @ResponseBody String updateUser(@RequestBody UserEntity newUser, HttpServletResponse res) {
		if(userService.updateUserInfo(newUser)) {
			return "success";
		} else {
			res.setStatus(400);
			return "failed";
		}
	}

	// 公开加载用户信息
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public @ResponseBody UserEntity getAllUserInfo(@RequestParam Integer userId, HttpServletResponse res) {
		UserEntity user = userService.getUser(userId);
		if(user == null) {
			res.setStatus(404);
		}
		return user;
	}
}
