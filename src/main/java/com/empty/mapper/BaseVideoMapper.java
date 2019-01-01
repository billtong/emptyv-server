package com.empty.mapper;

import java.util.List;

import com.empty.entity.VideoEntity;

/**
 * 视频持久化接口
 * @author Bill Tong
 * @Date 2018年12月29日
 */
public interface BaseVideoMapper {

	VideoEntity findVideoById(Integer videoId);
	
	List<VideoEntity> findVideosByName(String videoName);
	
	void saveNewVideo(VideoEntity video);
	
	void deleteVideoById(Integer videoId);
	
	void updateVideo(VideoEntity video);
	
}
