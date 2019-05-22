package com.empty.service.impl;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.empty.entity.UserEntity;
import com.empty.dao.BaseUserMapper;
import com.empty.service.BaseUserService;
import com.empty.util.MySessionContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("userService")
public class BaseUseServiceImpl implements BaseUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseUseServiceImpl.class);

    @Autowired
    BaseUserMapper userMapper;

    //cache userEntity for each userId
    @Autowired
    RedisTemplate redisTemplate;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public UserEntity getUser(Integer userId) {
        String userKey = "user_" + userId;
        ValueOperations<String, UserEntity> operations = redisTemplate.opsForValue();
        boolean hasUserKey = redisTemplate.hasKey(userKey);
        UserEntity user = hasUserKey ? operations.get(userKey) : userMapper.selectUserById(userId);
        if (!hasUserKey) {
            operations.set(userKey, user, 2, TimeUnit.SECONDS);
        }
        if (user != null) {
            user.setUserPassword(null);
            user.setUserEmail(null);
            user.setUserPerm(null);
        }
        return user;
    }

    @Transactional
    @Override
    public boolean registerNewUser(UserEntity userEntity, HashMap<String, String> message) {
        // 检查是否有重复用户名或邮箱名
        if (userMapper.findUserByName(userEntity.getUserName()) == null
                && userMapper.findUserByEmail(userEntity.getUserEmail()) == null) {
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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public boolean checkUserPassword(String userName, String userPassword) {
        UserEntity user = userMapper.findUserByName(userName);
        // 该用户不存在返回失败 false
        if (user == null) {
            return false;
        }
        // 匿名用户不能通过token check
        if (user.getUserId() == 0) {
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

    @Transactional
    @Override
    public boolean updateUserInfo(UserEntity user) {
        if (user == null) {
            return false;
        }
        userMapper.updateUser(user);
        String key = "user_" + user.getUserId();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
        }
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public boolean checkUserToken(Integer userId, String token, String sessionId) {
        String userKey = "user_" + userId;
        ValueOperations<String, UserEntity> operations = redisTemplate.opsForValue();
        boolean hasUserKey = redisTemplate.hasKey(userKey);

        UserEntity user = hasUserKey ? operations.get(userKey) : userMapper.selectUserById(userId);
        if (!hasUserKey) {
            operations.set(userKey, user, 2, TimeUnit.SECONDS);
        }
        HttpSession session = MySessionContext.getInstance().getSession(sessionId);
        String tokenCorrect = (String) session.getAttribute(user.getUserName());
        if (token.equals(tokenCorrect)) {
            return true;
        } else {
            System.out.println("token wrong拦截成功了！！！");
            return false;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public UserEntity getUserByName(String userName) {
        return userMapper.findUserByName(userName);
    }

}
