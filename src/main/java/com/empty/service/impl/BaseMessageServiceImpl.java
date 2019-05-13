package com.empty.service.impl;

import java.util.List;

import com.empty.entity.MessageEntity;
import com.empty.dao.BaseMessageMapper;
import com.empty.service.BaseMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("messageService")
public class BaseMessageServiceImpl implements BaseMessageService {

    @Autowired
    BaseMessageMapper msgMapper;

    @Resource(name = "userService")
    BaseUseServiceImpl userService;

    private boolean checkUserIsSender(Integer userId, Integer senderId) {
        return userId.equals(senderId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<MessageEntity> getMsgListByUserId(Integer userId) {
        List<MessageEntity> list = msgMapper.getMsgListByUserId(userId);
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
            return true;
        }
        return false;
    }
}