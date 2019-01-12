package com.empty.service;

import java.util.List;

import com.empty.entity.VideoEntity;

public interface BaseVideoService {
	
	
	List<VideoEntity> searchVideoByName(String videoName);
	
	List<VideoEntity> getRamdomVideos(Integer offset);
	
	/*
	 * 从表中根据id查到视频item
	 * 如果存在就将view-num+1
	 */
	VideoEntity viewVideoById(Integer videoId);
	
	void saveNewVideo(VideoEntity video);
	
	void deleteVideoById(Integer videoId);
}
