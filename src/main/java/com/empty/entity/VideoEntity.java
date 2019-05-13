package com.empty.entity;

import java.util.Date;

public class VideoEntity {
    private Integer videoId; // 自动增加
    private String videoName; // 最长50
    private String videoSrc; // mp4, m3u8等可以
    private Integer userId; // 发布用户id号
    private Date videoDate; // 默认是后端部署的电脑系统时间
    private String videoBadge; // 未来可能使用，类似头衔之类的
    private String videoDesc; // 视频描述，可以没有
    private String videoTag; // 视频标签，可以没有
    private String videoThumbnailImg; // 视频的图像，可以没有
    private String videoViewNum; // 点击数，默认为‘0’
    private String videoLikeNum; // 点赞数，默认为‘0’
    private String videoUnlikeNum; // 倒赞数，默认为‘0’
    private String videoFavouriteNum; // 收藏数，默认为‘0’
    private String videoDanmuNum; // 弹幕数，默认为‘0’
    private String videoCommentNum; // 评论数，默认为‘0’

    private UserEntity userInfo;    //getVideoList时生成;

    public String getVideoBadge() {
        return videoBadge;
    }

    public void setVideoBadge(String videoBadge) {
        this.videoBadge = videoBadge;
    }

    public String getVideoLikeNum() {
        return videoLikeNum;
    }

    public void setVideoLikeNum(String videoLikeNum) {
        this.videoLikeNum = videoLikeNum;
    }

    public String getVideoUnlikeNum() {
        return videoUnlikeNum;
    }

    public void setVideoUnlikeNum(String videoUnlikeNum) {
        this.videoUnlikeNum = videoUnlikeNum;
    }

    public String getVideoFavouriteNum() {
        return videoFavouriteNum;
    }

    public void setVideoFavouriteNum(String videoFavouriteNum) {
        this.videoFavouriteNum = videoFavouriteNum;
    }

    public String getVideoDanmuNum() {
        return videoDanmuNum;
    }

    public void setVideoDanmuNum(String videoDanmuNum) {
        this.videoDanmuNum = videoDanmuNum;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoSrc() {
        return videoSrc;
    }

    public void setVideoSrc(String videoSrc) {
        this.videoSrc = videoSrc;
    }

    public Date getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(Date videoDate) {
        this.videoDate = videoDate;
    }

    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }

    public String getVideoTag() {
        return videoTag;
    }

    public void setVideoTag(String videoTag) {
        this.videoTag = videoTag;
    }

    public String getVideoViewNum() {
        return videoViewNum;
    }

    public void setVideoViewNum(String videoViewNum) {
        this.videoViewNum = videoViewNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getVideoThumbnailImg() {
        return videoThumbnailImg;
    }

    public void setVideoThumbnailImg(String videoThumbnailImg) {
        this.videoThumbnailImg = videoThumbnailImg;
    }

    public String getVideoCommentNum() {
        return videoCommentNum;
    }

    public void setVideoCommentNum(String videoCommentNum) {
        this.videoCommentNum = videoCommentNum;
    }

    public UserEntity getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserEntity userInfo) {
        this.userInfo = userInfo;
    }
}
