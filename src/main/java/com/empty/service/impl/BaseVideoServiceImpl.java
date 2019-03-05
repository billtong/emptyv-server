package com.empty.service.impl;

import java.util.HashMap;
import java.util.List;
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
	public List<VideoEntity> searchVideoByName(String videoName) {
		return videoMapper.findVideosByName(videoName);
	}

	@Override
	public VideoEntity viewVideoById(Integer videoId) {
		VideoEntity video = videoMapper.findVideoById(videoId);
		if(video!=null) {
			video.setVideoViewNum(DataTools.stringAdder(video.getVideoViewNum()));
			videoMapper.updateVideo(video);
		}
		return video;
	}
	
	@Override
	public void saveNewVideo(VideoEntity video) {
		videoMapper.saveNewVideo(video);
	}

	@Override
	public void deleteVideoById(Integer videoId) {
		videoMapper.deleteVideoById(videoId);
	}

	@Override
	public Map<String, Object> getVideos(Integer currPage) {
		Map<String, Object> videoMap = new HashMap<>();
		int offset = (currPage-1) * VIDEOS_PAGE_SIZE; 
		int totalVideos = videoMapper.findVideoNum();
		if(offset >= 0 && offset < totalVideos) {
			int totalPages = totalVideos % VIDEOS_PAGE_SIZE == 0 ? totalVideos/VIDEOS_PAGE_SIZE : totalVideos/VIDEOS_PAGE_SIZE + 1; 
			videoMap.put("totalPages", totalPages);
			videoMap.put("videoList", videoMapper.selectLatestLimitVideos(offset));
			videoMap.put("message", "success");	
		} else {
			videoMap.put("message", "page number error");
		}
		return videoMap;
	}
}
