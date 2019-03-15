package com.empty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.HistoryEntity;
import com.empty.mapper.BaseCommentMapper;
import com.empty.mapper.BaseVideoMapper;
import com.empty.mapper.HistoryMapper;

@Service("historyService")
public class HistoryService {

	@Autowired
	HistoryMapper historyMapper;

	@Autowired
	BaseVideoMapper videoMapper;

	@Autowired
	BaseCommentMapper commentMapper;

	public List<HistoryEntity> getHistoryList(Integer userId) {
		List<HistoryEntity> hists = historyMapper.findByUserId(userId);
		for (int i = 0; i < hists.size(); i++) {
			HistoryEntity his = hists.get(i);
			his.setVideo(videoMapper.findVideoById(hists.get(i).getVideoId()));
			if(hists.get(i).getCommentId() != null) {
				his.setComment(commentMapper.selectCommentById(hists.get(i).getCommentId()));	
			}
			hists.set(i, his);
		}
		return hists;
	}

	public void saveNewHistory(Integer userId, int action, Integer videoId, Integer commentId) {
		historyMapper.saveNewHistory(new HistoryEntity(userId, videoId, action, commentId));
	}

}
