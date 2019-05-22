package com.empty.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.empty.entity.CommentEntity;
import com.empty.entity.VideoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.empty.entity.HistoryEntity;
import com.empty.dao.BaseCommentMapper;
import com.empty.dao.BaseVideoMapper;
import com.empty.dao.HistoryMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("historyService")
public class HistoryService {

    @Autowired
    HistoryMapper historyMapper;

    @Autowired
    BaseVideoMapper videoMapper;

    @Autowired
    BaseCommentMapper commentMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HistoryEntity> getHistoryList(Integer userId) {
        String historyListKey = "history_list_"+userId;
        Boolean hasHistoryListKey = redisTemplate.hasKey(historyListKey);
        ValueOperations<String, List<HistoryEntity>> operations1 = redisTemplate.opsForValue();
        List<HistoryEntity> hists = hasHistoryListKey ? operations1.get(historyListKey) : historyMapper.findByUserId(userId);
        if(!hasHistoryListKey) {
            operations1.set(historyListKey, hists, 2, TimeUnit.SECONDS);
        }
        for (int i = 0; i < hists.size(); i++) {
            HistoryEntity his = hists.get(i);
            String videoKey = "video_" + his.getVideoId();
            ValueOperations<String, VideoEntity> operations2 = redisTemplate.opsForValue();
            Boolean hasVideoKey = redisTemplate.hasKey(videoKey);
            VideoEntity video = hasVideoKey ? operations2.get(videoKey) : videoMapper.findVideoById(his.getVideoId());
            if(!hasVideoKey) {
                operations2.set(videoKey, video,2, TimeUnit.SECONDS);
            }
            his.setVideo(video);
            if (his.getCommentId() != null) {
                String commentKey = "comment_" +his.getCommentId();
                Boolean hasCommentKey = redisTemplate.hasKey(commentKey);
                ValueOperations<String, CommentEntity> operations3 = redisTemplate.opsForValue();
                CommentEntity comment = hasCommentKey ? operations3.get(commentKey) : commentMapper.selectCommentById(his.getCommentId());
                if(!hasCommentKey) {
                    operations3.set(commentKey, comment,2, TimeUnit.SECONDS);
                }
                his.setComment(comment);
            }
            hists.set(i, his);
        }
        return hists;
    }

    @Transactional
    public void saveNewHistory(Integer userId, int action, Integer videoId, Integer commentId) {
        HistoryEntity history = new HistoryEntity();
        history.setUserId(userId);
        history.setAction(action);
        history.setVideoId(videoId);
        history.setCommentId(commentId);
        historyMapper.saveNewHistory(history);
        String historyListKey = "history_list_"+userId;
        Boolean hasHistoryListKey = redisTemplate.hasKey(historyListKey);
        if(hasHistoryListKey) {
            redisTemplate.delete(historyListKey);
        }
    }
}
