package com.empty.service;

import java.util.List;

import com.empty.entity.CommentEntity;

public interface BaseCommentService {

	List<CommentEntity> searchCommentByVideoId(Integer videoId);

	void saveNewComment(CommentEntity comment, Integer userId);

	/**
	 * 如果是true ：能删除 如果是false：不能删除
	 * 
	 * @param comment
	 * @param userId
	 * @return
	 */
	boolean checkDeletePerms(Integer commentId, Integer userId);

	void deleteComment(Integer commentId);

}
