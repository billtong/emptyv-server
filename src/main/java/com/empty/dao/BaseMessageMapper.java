package com.empty.dao;

import java.util.List;

import com.empty.entity.MessageEntity;
import org.apache.ibatis.annotations.Mapper;

public interface BaseMessageMapper {

    MessageEntity getMsg(Integer msgId);

    List<MessageEntity> getMsgListByUserId(Integer userId);

    void saveNewMsg(MessageEntity messageEntity);

    void updateMsg(MessageEntity messageEntity);

    void deleteMsg(Integer msgId);

}
