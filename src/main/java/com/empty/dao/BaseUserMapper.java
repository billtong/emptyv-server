package com.empty.dao;

import com.empty.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

public interface BaseUserMapper {

    UserEntity selectUserById(Integer userId);

    UserEntity findUserByName(String userName);

    UserEntity findUserByEmail(String userEmail);

    void saveNewUser(UserEntity userEntity);

    void deleteUserById(Integer userId);

    void updateUser(UserEntity userEntity);

}
