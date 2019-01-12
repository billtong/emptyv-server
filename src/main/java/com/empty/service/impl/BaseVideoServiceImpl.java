package com.empty.service.impl;

import java.util.List;

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
	public List<VideoEntity> getRamdomVideos(Integer offset) {
		return videoMapper.selectLatestLimitVideos(offset);
	}

	
	

}
