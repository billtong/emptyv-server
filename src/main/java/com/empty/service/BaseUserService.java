package com.empty.service;

import com.empty.entity.UserEntity;

public interface BaseUserService {

	UserEntity getUserAll(Integer userId);
	
	/*
	 * 用于注册账户
	 * return ture表示成功
	 */
	boolean registerNewUser(UserEntity userEntity);
	
	/*
	 * 用于激活账户
	 * return ture表示成功
	 */
	boolean updateUserActivateState(String activatedCode);
	
	/*
	 * 用于登陆
	 * return ture表示成功
	 */
	boolean checkUserPassword(String userName, String userPassword);
	
	/*
	 * 用于更改信息
	 * return ture表示成功
	 */
	boolean updateUserInfo(UserEntity newUserEntity);
}
