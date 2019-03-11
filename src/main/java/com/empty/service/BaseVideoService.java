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
	Map<String, Object> getVideoList(Integer currPage, String word, String filter, Integer sizes);

	/**
	 * 
	 * @param videoId
	 * @return
	 */
	VideoEntity getVideoById(Integer videoId);

	/**
	 * 
	 * @param videoId
	 * @param action
	 * @return
	 */
	boolean actionVideo(Integer videoId, String action);

	/**
	 * 
	 * @param videoId
	 */
	void deleteVideoById(Integer videoId);
}
