package com.empty.entity;

import java.io.Serializable;
import java.util.Date;

public class DanEntity implements Serializable {
    private static final long serialVersionUID = -1L;

    private Integer danId;
    private Date danSendTime;
    private Integer danCurrTime;    //弹幕弹出时间单位是秒 和myVideoCurrentTime一样
    private String danContent;
    private String danStyle;    //这里可以直接存放css json对象
    private Integer videoId;
    private Integer userId;

    public Integer getDanId() {
        return danId;
    }

    public void setDanId(Integer danId) {
        this.danId = danId;
    }

    public Date getDanSendTime() {
        return danSendTime;
    }

    public void setDanSendTime(Date danSendTime) {
        this.danSendTime = danSendTime;
    }

    public String getDanContent() {
        return danContent;
    }

    public void setDanContent(String danContent) {
        this.danContent = danContent;
    }

    public String getDanStyle() {
        return danStyle;
    }

    public void setDanStyle(String danStyle) {
        this.danStyle = danStyle;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDanCurrTime() {
        return danCurrTime;
    }

    public void setDanCurrTime(Integer danCurrTime) {
        this.danCurrTime = danCurrTime;
    }
}
