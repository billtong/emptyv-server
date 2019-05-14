package com.empty.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.empty.entity.MessageEntity;
import com.empty.dao.BaseMessageMapper;
import com.empty.service.BaseMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("messageService")
public class BaseMessageServiceImpl implements BaseMessageService {

    @Autowired
    BaseMessageMapper msgMapper;

    //cache the messageEntity list for each userId;
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "userService")
    BaseUseServiceImpl userService;

    private boolean checkUserIsSender(Integer userId, Integer senderId) {
        return userId.equals(senderId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
    @Override
    public List<MessageEntity> getMsgListByUserId(Integer userId) {
        String msgListKey = "msg_list_" + userId;
        ValueOperations<String, List<MessageEntity>> operations = redisTemplate.opsForValue();
        boolean hasMsgListKey = redisTemplate.hasKey(msgListKey);
        List<MessageEntity> list = hasMsgListKey ? operations.get(msgListKey) : msgMapper.getMsgListByUserId(userId);
        if(!hasMsgListKey) {
            operations.set(msgListKey, list,2, TimeUnit.SECONDS);
        }
        for (MessageEntity me : list) {
            me.setSenderInfo(userService.getUser(me.getSenderId()));
            me.setListenerInfo(userService.getUser(me.getListenerId()));
        }
        return list;
    }

    @Transactional
    @Override
    public boolean saveNewMsg(MessageEntity newMe, Integer userId) {
        if (newMe != null && newMe.getMsgContent() != null && newMe.getListenerId() != null && newMe.getSenderId() != null) {
            if (newMe.getListenerId() > 0 && newMe.getSenderId() > 0) {
                if (this.checkUserIsSender(userId, newMe.getSenderId())) {
                    msgMapper.saveNewMsg(newMe);
                    String msgListKey = "msg_list_" + userId;
                    deleteCache(msgListKey);
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateMsg(MessageEntity newMe, Integer userId) {
        if (newMe.getMsgContent() != null && !newMe.getMsgContent().equals("")) {
            if (this.checkUserIsSender(userId, newMe.getSenderId())) {
                if (msgMapper.getMsg(newMe.getMsgId()) != null) {
                    msgMapper.updateMsg(newMe);
                    String key = "msg_list_" + userId;
                    deleteCache(key);
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean deleteMsg(Integer msgId, Integer userId) {
        MessageEntity me = msgMapper.getMsg(msgId);
        if (me != null && this.checkUserIsSender(userId, me.getSenderId())) {
            msgMapper.deleteMsg(msgId);
            String key = "msg_list_" + userId;
            deleteCache(key);
            return true;
        }
        return false;
    }

    private void deleteCache(String key) {
        if(redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

}