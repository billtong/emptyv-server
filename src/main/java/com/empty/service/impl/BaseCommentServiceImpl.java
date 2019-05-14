package com.empty.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.empty.entity.CommentEntity;
import com.empty.entity.VideoEntity;
import com.empty.dao.BaseCommentMapper;
import com.empty.dao.BaseUserMapper;
import com.empty.dao.BaseVideoMapper;
import com.empty.service.BaseCommentService;
import com.empty.service.HistoryService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("commentService")
public class BaseCommentServiceImpl implements BaseCommentService {

    @Autowired
    BaseCommentMapper baseCommentMapper;

    @Autowired
    BaseVideoMapper baseVideoMapper;

    //cache the commentEmtity list for each videoId; commentEntity for each commentId
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "userService")
    BaseUseServiceImpl userService;

    @Autowired
    HistoryService historyService;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<List<CommentEntity>> searchCommentByVideoId(Integer videoId) {
        String commentListKey = "comment_list_" + videoId;
        ValueOperations<String, List<CommentEntity>> operations = redisTemplate.opsForValue();
        Boolean hasCommentListKey = redisTemplate.hasKey(commentListKey);
        List<CommentEntity> rawList = hasCommentListKey ? operations.get(commentListKey) : baseCommentMapper.selectCommentsByVideoId(videoId);
        if(!hasCommentListKey) {
            operations.set(commentListKey, rawList,2, TimeUnit.SECONDS);
        }

        Iterator<CommentEntity> rawIte = rawList.iterator();
        List<List<CommentEntity>> commentListContainer = new LinkedList<>();
        Map<Integer, Integer> idMap = new HashMap<>(); // 储存comment楼层对应的comment id号
        while (rawIte.hasNext()) {
            CommentEntity ce = rawIte.next();
            ce.setUserInfo(userService.getUser(ce.getUserId()));
            if (ce.getCommentParentId().equals(0)) {
                List<CommentEntity> newl = new LinkedList<>();
                newl.add(ce);
                commentListContainer.add(newl);
                idMap.put(ce.getCommentId(), commentListContainer.size() - 1);
            } else {
                Integer index = idMap.get(ce.getCommentParentId());
                commentListContainer.get(index).add(ce);
                idMap.put(ce.getCommentId(), index);
            }
        }
        Collections.reverse(commentListContainer);
        return commentListContainer;
    }

    @Transactional
    @Override
    public void saveNewComment(CommentEntity comment, Integer userId) {
        baseCommentMapper.saveNewComment(comment);
        String commentListKey = "comment_list_" + comment.getVideoId();
        deleteCache(commentListKey);
        if (comment.getVideoId() != 0) {
            historyService.saveNewHistory(userId, 5, comment.getVideoId(), comment.getCommentId());
        }
    }

    @Transactional
    @Override
    public void saveNewCommentA(CommentEntity comment) {
        comment.setUserId(0);
        comment.setVideoId(0);
        baseCommentMapper.saveNewComment(comment);
        String commentListKey = "comment_list_" + comment.getVideoId();
        deleteCache(commentListKey);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public boolean checkDeletePerms(Integer commentId, Integer userId) {
        String commentKey = "comment_" + commentId;
        Boolean hasCommentKey = redisTemplate.hasKey(commentKey);
        ValueOperations<String, CommentEntity> operations1 = redisTemplate.opsForValue();
        CommentEntity comment = hasCommentKey ? operations1.get(commentKey) : baseCommentMapper.selectCommentById(commentId);
        if(!hasCommentKey) {
            operations1.set(commentKey, comment,2, TimeUnit.SECONDS);
        }

        String videoKey = "video_" + comment.getCommentId();
        ValueOperations<String, VideoEntity> operations2 = redisTemplate.opsForValue();
        Boolean hasVideoKey = redisTemplate.hasKey(videoKey);
        VideoEntity video = hasVideoKey ? operations2.get(videoKey) : baseVideoMapper.findVideoById(comment.getVideoId());
        if(!hasVideoKey) {
            operations2.set(videoKey, video,2, TimeUnit.SECONDS);
        }

        if (video != null && video.getUserId().equals(userId)) {
            return true;
            // 用户是该评论发布的用户
        } else // 其他情况不能删除
            return comment.getUserId().equals(userId);
    }

    public Integer getRootId(CommentEntity ce, List<CommentEntity> list) {
        if (ce.getCommentParentId().equals(0)) {
            return ce.getCommentId();
        }
        for (CommentEntity item : list) {
            if (item.getCommentId().equals(ce.getCommentParentId())) {
                ce = item;
                return getRootId(ce, list);
            }
        }
        return null;
    }

    @Transactional
    @Override
    public void deleteComment(Integer commentId) {
        String commentKey = "comment_" + commentId;
        Boolean hasCommentKey = redisTemplate.hasKey(commentKey);
        ValueOperations<String, CommentEntity> operations1 = redisTemplate.opsForValue();
        CommentEntity ce = hasCommentKey ? operations1.get(commentKey) : baseCommentMapper.selectCommentById(commentId);
        if(!hasCommentKey) {
            operations1.set(commentKey, ce,2, TimeUnit.SECONDS);
        }

        String commentListKey = "comment_list_" + ce.getVideoId();
        Boolean hasCommentListKey = redisTemplate.hasKey(commentListKey);
        ValueOperations<String, List<CommentEntity>> operations2 = redisTemplate.opsForValue();
        List<CommentEntity> celist = hasCommentListKey ? operations2.get(commentListKey) : baseCommentMapper.selectCommentsByVideoId(ce.getVideoId());
        if(!hasCommentListKey) {
            operations2.set(commentListKey, celist,2, TimeUnit.SECONDS);
        }

        if (ce.getCommentParentId().equals(0)) {
            List<Integer> delList = new ArrayList<>();
            for (CommentEntity item : celist) {
                if (!ce.getCommentId().equals(item.getCommentId()) && ce.getCommentId().equals(getRootId(item, celist))) {
                    delList.add(item.getCommentId());
                }
            }
            for (Integer id : delList) {
                baseCommentMapper.deleteCommentById(id);
                deleteCache("comment_" + id);
                deleteCache(commentListKey);
            }
        } else {
            for (CommentEntity item : celist) {
                if (item.getCommentParentId().equals(ce.getCommentId())) {
                    item.setCommentParentId(ce.getCommentParentId());
                    baseCommentMapper.updateComment(item);
                    deleteCache("comment_" + item.getCommentId());
                    deleteCache(commentListKey);
                }
            }
        }
        baseCommentMapper.deleteCommentById(commentId);
        deleteCache(commentKey);
        deleteCache(commentListKey);
    }

    private void deleteCache(String key) {
        if(redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }
}
