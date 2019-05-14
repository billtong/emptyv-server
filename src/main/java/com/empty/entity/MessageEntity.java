package com.empty.entity;

import java.io.Serializable;
import java.util.Date;

public class MessageEntity implements Serializable {
    private static final long serialVersionUID = -1L;

    private Integer msgId;
    private String msgContent;
    private String msgType;     //  默认是文本信息 “text”， 其他的信息on the way
    private Date msgTime;
    private Integer senderId;     //
    private Integer listenerId;   //  聊天对象Id

    private UserEntity senderInfo;
    private UserEntity listenerInfo;

    /**
     * @return the msgId
     */
    public Integer getMsgId() {
        return msgId;
    }

    /**
     * @param msgId the msgId to set
     */
    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    /**
     * @return the msgType
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * @param msgType the msgType to set
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * @return the msgTime
     */
    public Date getMsgTime() {
        return msgTime;
    }

    /**
     * @param msgTime the msgTime to set
     */
    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }

    /**
     * @return the senderId
     */
    public Integer getSenderId() {
        return senderId;
    }

    /**
     * @param senderId the senderId to set
     */
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    /**
     * @return the listenerId
     */
    public Integer getListenerId() {
        return listenerId;
    }

    /**
     * @param listenerId the listenerId to set
     */
    public void setListenerId(Integer listenerId) {
        this.listenerId = listenerId;
    }

    /**
     * @return the msgContent
     */
    public String getMsgContent() {
        return msgContent;
    }

    /**
     * @param msgContent the msgContent to set
     */
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public UserEntity getSenderInfo() {
        return senderInfo;
    }

    public void setSenderInfo(UserEntity senderInfo) {
        this.senderInfo = senderInfo;
    }

    public UserEntity getListenerInfo() {
        return listenerInfo;
    }

    public void setListenerInfo(UserEntity listenerInfo) {
        this.listenerInfo = listenerInfo;
    }
}