package com.empty.entity;

import java.util.Date;
import java.util.Map;

public class CommentEntity {
	private Integer commentId;
	private Integer commentParentId;	//0为根评论
	private String commentContent;
	private Date commentDate;
	private Integer videoId;
	private Integer userId;
	
	//在返回comment传时注入
	private UserEntity userInfo;
	
	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getCommentParentId() {
		return commentParentId;
	}

	public void setCommentParentId(Integer commentParentId) {
		this.commentParentId = commentParentId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
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

	public UserEntity getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserEntity userInfo) {
		this.userInfo = userInfo;
	}
}
