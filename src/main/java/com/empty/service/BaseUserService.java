package com.empty.service;

import java.util.HashMap;

import com.empty.entity.UserEntity;

public interface BaseUserService {

    UserEntity getUser(Integer userId);

    /*
     * 用于注册账户 return ture表示成功
     */
    boolean registerNewUser(UserEntity userEntity, HashMap<String, String> message);


    /*
     * 用于登陆 return ture表示成功
     */
    boolean checkUserPassword(String userName, String userPassword);

    /*
     * 用于更改信息 return ture表示成功
     */
    boolean updateUserInfo(UserEntity newUserEntity);

    boolean checkUserToken(Integer userId, String token, String sessionId);

    UserEntity getUserByName(String userName);

}
