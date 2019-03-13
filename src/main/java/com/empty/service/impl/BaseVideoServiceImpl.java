package com.empty.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.VideoEntity;
import com.empty.mapper.BaseVideoMapper;
import com.empty.service.BaseVideoService;
import com.empty.util.DataTools;

@Service("videoService")
public class BaseVideoServiceImpl implements BaseVideoService {

	@Autowired
	private BaseVideoMapper videoMapper;

	@Override
	public VideoEntity getVideoById(Integer videoId) {
		VideoEntity video = videoMapper.findVideoById(videoId);
		return video;
	}

	@Override
	public Map<String, Object> getVideos(Integer currPage, String word, String filter, Integer sizes) {
		Map<String, Object> videoMap = new HashMap<>();
		if (sizes >= 0) {
			int offset = (currPage - 1) * sizes;
			Map<String, Object> getVideosSqlMap = new HashMap<>();
			getVideosSqlMap.put("offset", offset);
			getVideosSqlMap.put("sizes", sizes);
			if (!filter.equals("date") && !filter.equals("view") && !filter.equals("rate")) {
				filter = "date"; // 默认日期为filter
			}
			getVideosSqlMap.put("filter", filter);
			getVideosSqlMap.put("word", word);
			Map<String, Object> getNumSqlMap = new HashMap<>();
			getNumSqlMap.put("word", word);
			int totalVideos = videoMapper.findVideoNum(getNumSqlMap);
			if (offset >= 0 && offset < totalVideos) {
				int totalPages = totalVideos % sizes == 0 ? totalVideos / sizes : totalVideos / sizes + 1;
				videoMap.put("totalPages", totalPages);
				videoMap.put("videoList", videoMapper.selectVideos(getVideosSqlMap));
				videoMap.put("message", "success");
			}
		} else {
			videoMap.put("message", "failed geting videos");
		}
		return videoMap;
	}

	@Override
	public boolean videoAction(Integer videoId, String action) {
		if (videoId >= 1 && action != null) {
			VideoEntity video = videoMapper.findVideoById(videoId);
			if (video != null) {
				switch (action) {
				case "view":
					video.setVideoViewNum(DataTools.stringAdder(video.getVideoViewNum(), 1));
					break;
				case "like":
					video.setVideoLikeNum(DataTools.stringAdder(video.getVideoLikeNum(), 1));
					break;
				case "unlike":
					video.setVideoUnlikeNum(DataTools.stringAdder(video.getVideoUnlikeNum(), 1));
					break;
				case "favourite":
					video.setVideoFavouriteNum(DataTools.stringAdder(video.getVideoFavouriteNum(), 1));
					break;
				case "comment":
					video.setVideoCommentNum(DataTools.stringAdder(video.getVideoCommentNum(), 1));
					break;
				case "danmu":
					video.setVideoDanmuNum(DataTools.stringAdder(video.getVideoDanmuNum(), 1));
					break;
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
