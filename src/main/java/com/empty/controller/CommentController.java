package com.empty.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.empty.entity.CommentEntity;
import com.empty.service.BaseCommentService;

@RestController
@RequestMapping(value = "/api/comment", produces = "application/json;charset=UTF-8")
public class CommentController {

    @Resource(name = "commentService")
    BaseCommentService commentService;

    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public List<List<CommentEntity>> loadCommentByVideoId(@RequestParam Integer videoId) {
        return commentService.searchCommentByVideoId(videoId);
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String writeComment(@RequestBody CommentEntity comment, @RequestParam Integer userId) {
        commentService.saveNewComment(comment, userId);
        return "write success";
    }

    @RequestMapping(value = "/writeA", method = RequestMethod.POST)
    public String writeCommentA(@RequestBody CommentEntity comment) {
        commentService.saveNewCommentA(comment);
        return "write success";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteComment(@RequestParam Integer userId, @RequestParam Integer commentId, HttpServletResponse res) {
        if (commentService.checkDeletePerms(commentId, userId)) {
            commentService.deleteComment(commentId);
            res.setStatus(200);
        } else {
            res.setStatus(403);
        }
    }
}
