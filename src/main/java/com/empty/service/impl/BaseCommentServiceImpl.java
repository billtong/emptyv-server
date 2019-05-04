package com.empty.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.CommentEntity;
import com.empty.entity.UserEntity;
import com.empty.entity.VideoEntity;
import com.empty.mapper.BaseCommentMapper;
import com.empty.mapper.BaseUserMapper;
import com.empty.mapper.BaseVideoMapper;
import com.empty.service.BaseCommentService;
import com.empty.service.HistoryService;

@Service("commentService")
public class BaseCommentServiceImpl implements BaseCommentService {

	@Autowired
	BaseCommentMapper baseCommentMapper;

	@Autowired
	BaseUserMapper baseUserMapper;

	@Autowired
	BaseVideoMapper baseVideoMapper;

	@Resource(name = "userService")
	BaseUseServiceImpl userService;

	@Autowired
	HistoryService historyService;

	@Override
	public List<List<CommentEntity>> searchCommentByVideoId(Integer videoId) {
		List<CommentEntity> rawList = baseCommentMapper.selectCommentsByVideoId(videoId);
		Iterator<CommentEntity> rawIte = rawList.iterator();
		List<List<CommentEntity>> newll = new LinkedList<>();
		Map<Integer, Integer> idMap = new HashMap<>(); // 储存comment楼层对应的comment id号
		while (rawIte.hasNext()) {
			CommentEntity ce = rawIte.next();
			ce.setUserInfo(userService.getUser(ce.getUserId()));
			if (ce.getCommentParentId().equals(Integer.valueOf(0))) {
				List<CommentEntity> newl = new LinkedList<>();
				newl.add(ce);
				newll.add(newl);
				idMap.put(ce.getCommentId(), newll.size() - 1);
			} else {
				Integer index = idMap.get(ce.getCommentParentId());
				newll.get(index).add(ce);
				idMap.put(ce.getCommentId(), index);
			}
		}
		// 新的到前面去
		Collections.reverse(newll);
		return newll;
	}

	@Override
	public void saveNewComment(CommentEntity comment, Integer userId) {
		baseCommentMapper.saveNewComment(comment);
		if (comment.getVideoId() != 0) {
			historyService.saveNewHistory(userId, 5, comment.getVideoId(), comment.getCommentId());
		}
	}

	@Override
	public void saveNewCommentA(CommentEntity comment) {
		comment.setUserId(0);
		comment.setVideoId(0);
		baseCommentMapper.saveNewComment(comment);
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
