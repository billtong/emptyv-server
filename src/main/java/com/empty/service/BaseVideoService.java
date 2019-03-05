package com.empty.service;

import java.util.List;
import java.util.Map;

import com.empty.entity.VideoEntity;

public interface BaseVideoService {
	final static int VIDEOS_PAGE_SIZE = 20;
	
	List<VideoEntity> searchVideoByName(String videoName);
	
	Map<String, Object> getVideos(Integer currPage);
	
	/*
	 * 从表中根据id查到视频item
	 * 如果存在就将view-num+1
	 */
	VideoEntity viewVideoById(Integer videoId);
	
	void saveNewVideo(VideoEntity video);
	
	void deleteVideoById(Integer videoId);
}
