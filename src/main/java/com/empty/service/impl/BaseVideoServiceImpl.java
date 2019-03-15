package com.empty.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.VideoEntity;
import com.empty.entity.HistoryEntity;
import com.empty.mapper.BaseVideoMapper;
import com.empty.mapper.HistoryMapper;
import com.empty.service.BaseVideoService;
import com.empty.service.HistoryService;
import com.empty.util.DataTools;

@Service("videoService")
public class BaseVideoServiceImpl implements BaseVideoService {

	@Autowired
	private BaseVideoMapper videoMapper;
	
	@Resource(name = "historyService")
	HistoryService historyService;

	@Override
	public VideoEntity getVideoById(Integer videoId) {
		VideoEntity video = videoMapper.findVideoById(videoId);
		return video;
	}

	@Override
	public Map<String, Object> getVideos(String word, String filter) {
		Map<String, Object> videoMap = new HashMap<>();
		Map<String, Object> getVideosSqlMap = new HashMap<>();
		if (!filter.equals("date") && !filter.equals("view") && !filter.equals("rate")) {
			filter = "date"; // 默认日期为filter
		}
		getVideosSqlMap.put("filter", filter);
		getVideosSqlMap.put("word", word);
		videoMap.put("videoList", videoMapper.selectVideos(getVideosSqlMap));
		return videoMap;
	}

	@Override
	public boolean videoAction(Integer videoId, String action,Integer userId) {
		if (videoId >= 1 && action != null) {
			VideoEntity video = videoMapper.findVideoById(videoId);
			if (video != null) {
				switch (action) {
				case "view":
					video.setVideoViewNum(DataTools.stringAdder(video.getVideoViewNum(), 1));
					if(userId != null) {
						historyService.saveNewHistory(userId, HistoryEntity.histoyActionCode.View.ordinal(), videoId, null);	
					}
					break;
				case "like":
					video.setVideoLikeNum(DataTools.stringAdder(video.getVideoLikeNum(), 1));
					historyService.saveNewHistory(userId, HistoryEntity.histoyActionCode.Like.ordinal(), videoId, null);
					break;
				case "unlike":
					video.setVideoUnlikeNum(DataTools.stringAdder(video.getVideoUnlikeNum(), 1));
					historyService.saveNewHistory(userId, HistoryEntity.histoyActionCode.UnLike.ordinal(), videoId, null);
					break;
				case "favourite":
					video.setVideoFavouriteNum(DataTools.stringAdder(video.getVideoFavouriteNum(), 1));
					historyService.saveNewHistory(userId, HistoryEntity.histoyActionCode.Favourite.ordinal(), videoId, null);
					break;
					
					/*转移到前端
				case "comment":
					video.setVideoCommentNum(DataTools.stringAdder(video.getVideoCommentNum(), 1));
					break;
				case "danmu":
					video.setVideoDanmuNum(DataTools.stringAdder(video.getVideoDanmuNum(), 1));
					break;
					*/
				default:
					return false;
				}
				videoMapper.updateVideo(video);
				return true;
			}
		}
		return false;
	}

	/**
	 * 还没使用
	 */
	@Override
	public void deleteVideoById(Integer videoId) {
		videoMapper.deleteVideoById(videoId);
	}

	@Override
	public boolean updateTags(Integer videoId, String tagJsonString) {
		VideoEntity video = videoMapper.findVideoById(videoId);
		if (video != null && tagJsonString != null) {
			video.setVideoTag(tagJsonString);
			videoMapper.updateVideo(video);
			
			return true;
		}
		return false;
	}
}
