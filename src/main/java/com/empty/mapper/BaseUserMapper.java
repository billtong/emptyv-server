package com.empty.mapper;

import com.empty.entity.UserEntity;

public interface BaseUserMapper {

	UserEntity selectUserById(Integer userId);

	UserEntity findUserByName(String userName);

	UserEntity findUserByEmail(String userEmail);

	void saveNewUser(UserEntity userEntity);

	void deleteUserById(Integer userId);

	void updateUser(UserEntity userEntity);

}
