package com.empty.entity;

import java.util.Date;
import java.util.List;

public class CommentEntity {
	private Integer commentId;
	private Integer commentParentId;
	private String commentContent;
	private Date commentDate;
	private Integer videoId;
	private Integer userId;

	/**
	 * 注意: 子评论（commentChild）是在service层生成的，没有持久化
	 */
	private List<CommentEntity> commentChild;

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

	public List<CommentEntity> getCommentChild() {
		return commentChild;
	}

	public void setCommentChild(List<CommentEntity> commentChild) {
		this.commentChild = commentChild;
	}
}
