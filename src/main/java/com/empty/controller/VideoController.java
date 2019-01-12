package com.empty.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empty.entity.VideoEntity;
import com.empty.service.BaseVideoService;

@Controller
@RequestMapping(value = "/api/video", produces = "application/json;charset=UTF-8")
public class VideoController {

	@Resource(name = "videoService")
	BaseVideoService videoService;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody List<VideoEntity> searchVideo(@RequestParam String words) {
		return videoService.searchVideoByName(words);
	}
	
	@RequestMapping(value = "/random", method = RequestMethod.GET)
	public @ResponseBody List<VideoEntity> getRandomVideos(@RequestParam Integer offset) {
		return videoService.getRamdomVideos(offset);
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public @ResponseBody VideoEntity getVideoById(@RequestParam Integer videoId) {
		return videoService.viewVideoById(videoId);
	}

}
