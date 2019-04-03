package com.empty.service.impl;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.UserEntity;
import com.empty.mapper.BaseUserMapper;
import com.empty.service.BaseUserService;
import com.empty.util.MySessionContext;

@Service("userService")
public class BaseUseServiceImpl implements BaseUserService {

	@Autowired
	BaseUserMapper userMapper;

	@Override
	public UserEntity getUserAll(Integer userId) {
		UserEntity user = userMapper.selectUserById(userId);
		return user;
	}

	@Override
	public boolean registerNewUser(UserEntity userEntity, HashMap<String, String> message) {
		// 检查是否有重复用户名或邮箱名
		if (userMapper.findUserByName(userEntity.getUserName()) == null
				&& userMapper.findUserByEmail(userEntity.getUserEmail()) == null) {
			/*
			 * // 发送激活邮件 try {
			 * MailUtil.sendTo(MailUtil.generateRegisterMailBody(userEntity),
			 * userEntity.getUserEmail()); } catch (Exception e) { // TODO Auto-generated
			 * catch block e.printStackTrace(); return false; } // 如果邮件发送成功的话，填表然后返回R true
			 */
			userMapper.saveNewUser(userEntity);
			message.put("message", "success. You can log in now!");
			return true;
		}
		if (userMapper.findUserByName(userEntity.getUserName()) != null) {
			message.put("message", "the username is used already.");
			return false;
		}
		if (userMapper.findUserByEmail(userEntity.getUserEmail()) != null) {
			message.put("message", "the email is used already.");
			return false;
		}
		return false;
	}


	@Override
	public boolean checkUserPassword(String userName, String userPassword) {
		UserEntity user = userMapper.findUserByName(userName);
		// 该用户不存在返回失败 false
		if (user == null) {
			return false;
		}
		// sql不区分大小写，这里必须判断一下
		if (!user.getUserName().equals(userName)) {
			return false;
		}
		// 该用户密码和提供的密码匹配上了 返回true成功
		if (user.getUserPassword().equals(userPassword)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateUserInfo(UserEntity newUserEntity) {
		UserEntity user = userMapper.selectUserById(newUserEntity.getUserId());
		// 该用户不存在返回失败 false
		if (user == null) {
			return false;
		}
		userMapper.updateUser(newUserEntity);
		return true;
	}

	@Override
	public boolean checkUserToken(Integer userId, String token, String sessionId) {

		UserEntity user = userMapper.selectUserById(userId);
		HttpSession session = MySessionContext.getInstance().getSession(sessionId);
		String tokenCorrect = (String) session.getAttribute(user.getUserName());
		if (token.equals(tokenCorrect)) {
			return true;
		} else {
			System.out.println("token wrong拦截成功了！！！");
			return false;
		}
	}

	@Override
	public UserEntity getUserByName(String userName) {
		return userMapper.findUserByName(userName);
	}

}
