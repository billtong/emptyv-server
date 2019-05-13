package com.empty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HistoryEntity> getHistoryList(Integer userId) {
        List<HistoryEntity> hists = historyMapper.findByUserId(userId);
        for (int i = 0; i < hists.size(); i++) {
            HistoryEntity his = hists.get(i);
            his.setVideo(videoMapper.findVideoById(hists.get(i).getVideoId()));
            if (hists.get(i).getCommentId() != null) {
                his.setComment(commentMapper.selectCommentById(hists.get(i).getCommentId()));
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
    }

}
