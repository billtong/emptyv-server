package com.empty.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.CommentEntity;
import com.empty.entity.VideoEntity;
import com.empty.mapper.BaseCommentMapper;
import com.empty.mapper.BaseVideoMapper;
import com.empty.service.BaseCommentService;

@Service("commentService")
public class BaseCommentServiceImpl implements BaseCommentService {

	@Autowired
	BaseCommentMapper baseCommentMapper;

	@Autowired
	BaseVideoMapper baseVideoMapper;

	@Override
	public List<CommentEntity> searchCommentByVideoId(Integer videoId) {
		List<CommentEntity> rawList = baseCommentMapper.selectCommentsByVideoId(videoId);
		return rawList;
	}

	@Override
	public void saveNewComment(CommentEntity comment) {
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
		
		/*
		CommentEntity comment = baseCommentMapper.selectCommentById(commentId);
		
		
		
		//判断这条评论有没有回复，不然要递归删除
		if (comment.getCommentParentId() == 0) {
			List<CommentEntity> rawList = baseCommentMapper.selectCommentsByVideoId(comment.getVideoId());
			rawList = getRsltList(rawList);
			for (CommentEntity c : rawList) {
				if (c.getCommentId().equals(commentId)) {
					comment = c;
					break;
				}
			}
			for (int i = 0; i < comment.getCommentChild().size(); i++) {
				deleteComment(comment.getCommentChild().get(i).getCommentId());
			}
		}

		//没有回复的，直接用Mapper删除
		 */
		baseCommentMapper.deleteCommentById(commentId);
	}

	/*
	private List<CommentEntity> getRsltList(List<CommentEntity> rawList) {
		List<CommentEntity> rsltList = new ArrayList<>();
		List<Boolean> checkIsAdded = new ArrayList<>();

		
		//将每一个comment是否被装进去记录到 checkIsAdded list里
		for (int i = 0; i < rawList.size(); i++) {
			checkIsAdded.add(false);
		}

		
		for (int root = 0; root < rawList.size(); root++) {
			for (int child = root + 1; child < rawList.size(); child++) {

				if (rawList.get(root).getCommentId().equals(rawList.get(child).getCommentParentId())) {
					
					//初始化 commentChild list
					if (rawList.get(root).getCommentChild() == null) {
						List<CommentEntity> temp = new ArrayList<>();
						rawList.get(root).setCommentChild(temp);
					}
					
					//if判断防止重复添加child到rsltList里
					if (checkIsAdded.get(child) == false) {
						rawList.get(root).getCommentChild().add(rawList.get(child));
						checkIsAdded.set(child, true);
					}
				}
			}
			
			//if判断防止重复添加root到rsltList里
			if (checkIsAdded.get(root) == false) {
				rsltList.add(rawList.get(root));
				checkIsAdded.set(root, true);
			}
		}
		return rsltList;
	}
*/
}
