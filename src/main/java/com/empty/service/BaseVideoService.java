package com.empty.service;

import java.util.Map;

import com.empty.entity.VideoEntity;

public interface BaseVideoService {
	/**
	 * 查找字符传或者直接从整个数据库搜索
	 * 
	 * @param currPage
	 * @param word
	 * @param filter
	 * @return
	 */
	Map<String, Object> getVideos(Integer currPage, String word, String filter, Integer sizes);

	VideoEntity getVideoById(Integer videoId);
	
	boolean videoAction(Integer videoId, String action);

	boolean updateTags(Integer videoId, String tagJsonString);
	
	void deleteVideoById(Integer videoId);
}
