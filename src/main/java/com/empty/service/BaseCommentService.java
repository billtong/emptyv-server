package com.empty.service;

import java.util.List;

import com.empty.entity.CommentEntity;

public interface BaseCommentService {

    /**
     * 这里内部的list第一个是在root里的comment，
     * 之后的全部都是在这个comment下的评论
     *
     * @param videoId
     * @return
     */
    public List<List<CommentEntity>> searchCommentByVideoId(Integer videoId);

    void saveNewComment(CommentEntity comment, Integer userId);

    public void saveNewCommentA(CommentEntity comment);

    boolean checkDeletePerms(Integer commentId, Integer userId);

    void deleteComment(Integer commentId);

}
