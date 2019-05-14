package com.empty.dao;

import java.util.List;

import com.empty.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;

public interface BaseCommentMapper {

    CommentEntity selectCommentById(Integer commentId);

    List<CommentEntity> selectCommentsByVideoId(Integer videoId);

    void saveNewComment(CommentEntity comment);

    void deleteCommentById(Integer commentId);

    void updateComment(CommentEntity comment);

}
