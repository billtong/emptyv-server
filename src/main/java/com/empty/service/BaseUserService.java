package com.empty.service;

import java.util.HashMap;

import com.empty.entity.UserEntity;

public interface BaseUserService {

    UserEntity getUser(Integer userId);

    boolean registerNewUser(UserEntity userEntity, HashMap<String, String> message);

    boolean checkUserPassword(String userName, String userPassword);

    boolean updateUserInfo(UserEntity newUserEntity);

    boolean checkUserToken(Integer userId, String token, String sessionId);

    UserEntity getUserByName(String userName);

}
