package com.empty.entity;

import java.util.Date;

public class HistoryEntity {
	private Integer historyId;
	private Date historyTime;
	private String historyDesc;
	private Integer userId;
	private Integer videoId;
	private Integer commentId;

	public Integer getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}

	public Date getHistoryTime() {
		return historyTime;
	}

	public void setHistoryTime(Date historyTime) {
		this.historyTime = historyTime;
	}

	public String getHistoryDesc() {
		return historyDesc;
	}

	public void setHistoryDesc(String historyDesc) {
		this.historyDesc = historyDesc;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getVideoId() {
		return videoId;
	}

	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
}
