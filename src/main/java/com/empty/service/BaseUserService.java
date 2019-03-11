package com.empty.service;

import java.util.HashMap;

import com.empty.entity.UserEntity;

public interface BaseUserService {

	UserEntity getUserAll(Integer userId);

	/**
	 * 
	 * @param userEntity
	 * @param message
	 * @return
	 */
	boolean registerNewUser(UserEntity userEntity, HashMap<String, String> message);

	/**
	 * 
	 * @param activatedCode
	 * @return
	 */
	boolean updateUserActivateState(String activatedCode);

	/**
	 * 
	 * @param userName
	 * @param userPassword
	 * @return
	 */
	boolean checkUserPassword(String userName, String userPassword);

	/**
	 * 
	 * @param newUserEntity
	 * @return
	 */
	boolean updateUserInfo(UserEntity newUserEntity);

	/**
	 * 检查user的token
	 * 
	 * @param userId
	 * @param token
	 * @param sessionId
	 * @return
	 */
	boolean checkUserToken(Integer userId, String token, String sessionId);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	UserEntity getUserByName(String userName);

}
