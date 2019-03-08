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

	/**
	 * 注册endpoint 1.Body里要至少有userName userPassword userEmail
	 * 
	 * @param signMap
	 * @return
	 */
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

	/**
	 * 激活邮件里的链接 （舍弃） 1.Param里要有code
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/activated", method = RequestMethod.GET)
	public @ResponseBody String activatedNewUser(@RequestParam String code) {
		return userService.updateUserActivateState(code) ? "success" : "failed";
	}

	/**
	 * 登陆 1.Body里要有 userName，userPassword，userEmail 返回session id，用来匹配到相应的sessionid
	 * 
	 * @param session
	 * @param loginMap
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> userLogin(HttpSession session, HttpServletRequest req,
			HttpServletResponse res, @RequestBody HashMap<String, String> loginMap) {
		HashMap<String, Object> message = new HashMap<>();
		// 将token按照用户名字储存到服务器的session中，再将该token返回
		if (userService.checkUserPassword(loginMap.get("userName"), loginMap.get("userPassword"))) {
			String token = UUID.randomUUID().toString().replace("-", "");
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

	/**
	 * 登出，将sessionContext中储存的session删掉
	 * 
	 * @param userJson
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> userLogout(@RequestBody Map<String, String> userJson,
			HttpServletResponse res) {
		Map<String, String> message = new HashMap<>();
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
		return message;
	}

	/**
	 * 加载用户信息（被token check拦截） 1.Header里要有token 2.Param里要有userId
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	public @ResponseBody UserEntity getAllUserInfo(@RequestParam Integer userId) {
		return userService.getUserAll(userId);
	}
}
