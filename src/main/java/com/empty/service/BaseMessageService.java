package com.empty.service;

import java.util.List;

import com.empty.entity.MessageEntity;

public interface BaseMessageService {
    List<MessageEntity> getMsgListByUserId(Integer userId);

    boolean saveNewMsg(MessageEntity newMe, Integer userId);

    boolean updateMsg(MessageEntity newMe, Integer userId);

    boolean deleteMsg(Integer msgId, Integer userId);
}