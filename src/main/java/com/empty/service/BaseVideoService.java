package com.empty.service;

import java.util.Map;

import com.empty.entity.VideoEntity;

public interface BaseVideoService {
	
	//查找字符传或者直接从整个数据库搜索 
	Map<String, Object> getVideos(String word, String filter);

	VideoEntity getVideoById(Integer videoId);
	
	boolean videoAction(Integer videoId, String action,Integer userId);

	boolean updateTags(Integer videoId, String tagJsonString);
	
	void deleteVideoById(Integer videoId);
}
