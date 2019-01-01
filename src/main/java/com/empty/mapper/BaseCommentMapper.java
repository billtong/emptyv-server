package com.empty.mapper;

import java.util.List;

import com.empty.entity.CommentEntity;

public interface BaseCommentMapper {

	CommentEntity selectCommentById(Integer commentId);
	
	List<CommentEntity> selectCommentsByVideoId(Integer videoId);

	void saveNewComment(CommentEntity comment);

	void deleteCommentById(Integer commentId);
	
	
	//void updateComment(CommentEntity comment);

}
