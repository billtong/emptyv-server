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

	/*
	 * 从表中根据id查到视频item 如果存在就将view-num+1
	 */
	VideoEntity viewVideoById(Integer videoId);

	boolean videoAction(Integer videoId, String action);

	void deleteVideoById(Integer videoId);
}
