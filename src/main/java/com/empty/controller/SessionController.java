package com.empty.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empty.util.MySessionContext;

@Controller
@RequestMapping(value = "/test/session", produces = "application/json;charset=UTF-8")
public class SessionController {
	
	
	@RequestMapping(value = "/getAllSessions", method = RequestMethod.GET)
	public @ResponseBody Map<String,HttpSession> getAllSession () {
		return MySessionContext.getInstance().getSessionMap();
	}
}
