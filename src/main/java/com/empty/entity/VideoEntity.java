package com.empty.entity;

import java.util.Date;

public class VideoEntity {
	private Integer videoId;
	private String videoName;
	private String videoSrc;
	private Integer userId;
	private Date videoDate;
	private String videoDesc;
	private String videoTag;
	private String videoViewNum;
	private String videoThumbnailImg;
	
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
}
