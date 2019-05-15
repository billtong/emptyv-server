package com.empty.mapper;

import java.util.List;
import java.util.Map;

import com.empty.entity.VideoEntity;

/**
 * 视频持久化接口
 * 
 * @author Bill Tong
 * @Date 2018年12月29日
 */
public interface BaseVideoMapper {

	/**
	 * 根据视频ID获取唯一视频
	 * 
	 * @param videoId
	 * @return
	 */
	VideoEntity findVideoById(Integer videoId);

	/**
	 * 获得视频条目信息
	 * 
	 * @param map = { 
	 *					filter:(string), //[date, view, rate] 
	 *					word: (String) //匹配关键字 暂时指匹配title }
	 * @return
	 */
	List<VideoEntity> selectVideos(Map<String, Object> map);

	
	//获得视频条目数量
	Integer findVideoNum(Map<String, Object> map);

	/**
	 * 更新一个视频信息
	 * 
	 * @param video
	 */
	void updateVideo(VideoEntity video);

	void saveNewVideo(VideoEntity video);

	void deleteVideoById(Integer videoId);
}
