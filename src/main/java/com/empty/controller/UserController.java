package com.empty.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.empty.entity.UserEntity;
import com.empty.service.impl.BaseUseServiceImpl;
import com.empty.util.MySessionContext;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json;charset=UTF-8")
public class UserController {

    @Resource(name = "userService")
    BaseUseServiceImpl userService;

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public HashMap<String, String> userSignUp(HttpServletResponse res,
                                              @RequestBody HashMap<String, String> signMap) {
        HashMap<String, String> message = new HashMap<>();
        UserEntity newUser = new UserEntity();
        newUser.setUserName(signMap.get("userName"));
        newUser.setUserPassword(signMap.get("userPassword"));
        newUser.setUserEmail(signMap.get("userEmail"));
        userService.registerNewUser(newUser, message);
        return message;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public HashMap<String, Object> userLogin(HttpSession session, HttpServletRequest req,
                                             HttpServletResponse res, @RequestBody HashMap<String, String> loginMap) {
        HashMap<String, Object> message = new HashMap<>();
        // 将token按照用户名字储存到服务器的session中，再将该token返回
        if (userService.checkUserPassword(loginMap.get("userName"), loginMap.get("userPassword"))) {
            String token = UUID.randomUUID().toString().replace("-", "");
            session.setMaxInactiveInterval(7 * 24 * 60 * 60); //token保存的时间，即7日不上站就自己删掉了 7day=7*24*60*60
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

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Map<String, String> userLogout(@RequestBody Map<String, String> userJson,
                                          HttpServletResponse res) {
        Map<String, String> message = new HashMap<>();
        try {
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

    @RequestMapping(value = "update", method = RequestMethod.PATCH)
    public String updateUser(@RequestBody UserEntity newUser, HttpServletResponse res) {
        if (userService.updateUserInfo(newUser)) {
            return "success";
        } else {
            res.setStatus(400);
            return "failed";
        }
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public UserEntity getAllUserInfo(@RequestParam Integer userId, HttpServletResponse res) {
        UserEntity user = userService.getUser(userId);
        if (user == null) {
            res.setStatus(404);
        }
        return user;
    }
}
