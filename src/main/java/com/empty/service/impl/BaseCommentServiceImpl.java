package com.empty.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.CommentEntity;
import com.empty.entity.HistoryEntity;
import com.empty.entity.VideoEntity;
import com.empty.mapper.BaseCommentMapper;
import com.empty.mapper.BaseVideoMapper;
import com.empty.mapper.HistoryMapper;
import com.empty.service.BaseCommentService;
import com.empty.service.HistoryService;

@Service("commentService")
public class BaseCommentServiceImpl implements BaseCommentService {

	@Autowired
	BaseCommentMapper baseCommentMapper;

	@Autowired
	BaseVideoMapper baseVideoMapper;
	
	@Autowired
	HistoryService historyService;

	@Override
	public List<CommentEntity> searchCommentByVideoId(Integer videoId) {
		List<CommentEntity> rawList = baseCommentMapper.selectCommentsByVideoId(videoId);
		return rawList;
	}

	@Override
	public void saveNewComment(CommentEntity comment, Integer userId) {
		baseCommentMapper.saveNewComment(comment);
		historyService.saveNewHistory(userId, 5, comment.getVideoId(), comment.getCommentId());
	}

	@Override
	public boolean checkDeletePerms(Integer commentId, Integer userId) {
		CommentEntity comment = baseCommentMapper.selectCommentById(commentId);
		VideoEntity video = baseVideoMapper.findVideoById(comment.getVideoId());
		// 用户是该评论所在视频的up主
		if (video != null && video.getUserId().equals(userId)) {
			return true;
			// 用户是该评论发布的用户
		} else if (comment.getUserId().equals(userId)) {
			return true;
			// 其他情况不能删除
		} else {
			return false;
		}
	}

	@Override
	public void deleteComment(Integer commentId) {
		baseCommentMapper.deleteCommentById(commentId);
	}
}
