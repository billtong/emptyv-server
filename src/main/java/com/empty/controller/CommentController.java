package com.empty.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empty.entity.CommentEntity;
import com.empty.service.BaseCommentService;

@Controller
@RequestMapping(value = "/api/comment", produces = "application/json;charset=UTF-8")
public class CommentController {

	@Resource(name = "commentService")
	BaseCommentService commentService;

	
	/**
	 * 加载一个视频的所有评论
	 * 1.param里要有videoId
	 * @param videoId
	 * @return
	 */
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public @ResponseBody List<CommentEntity> loadCommentByVideoId(@RequestParam Integer videoId) {
		return commentService.searchCommentByVideoId(videoId);
	}
	
	/**
	 * 发布一条新评论（被token check拦截）
	 * 必须：
	 * 1.body里填上CommentEntity
	 * 2.Header里要有一个token
	 * 3.Param里要有一个userId
	 * @param comment
	 */
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public void writeComment(@RequestBody CommentEntity comment) {
		commentService.saveNewComment(comment);
	}
	
	/**
	 * 删除一条评论（被token check拦截）
	 * 1.Header里要有一个token
	 * 2.Param里要有 userId和commentId
	 * @param userId
	 * @param commentId
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteComment(@RequestParam Integer userId, @RequestParam Integer commentId) {
		if(commentService.checkDeletePerms(commentId, userId)) {
			commentService.deleteComment(commentId);
		}
	}
	

}
