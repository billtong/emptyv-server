package com.empty.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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

	@RequestMapping(value = "/getVideo", method = RequestMethod.GET)
	public @ResponseBody VideoEntity getVideoById(@RequestParam Integer videoId, HttpServletResponse res) {
		VideoEntity v = videoService.getVideoById(videoId);
		if (v != null) {
			return v;
		}
		res.setStatus(404);
		return null;
	}

	@RequestMapping(value = "/getVideoList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchVideo(@RequestParam String word, @RequestParam String filter,
			HttpServletResponse res) {
		return videoService.getVideos(word, filter);
	}

	//
	@RequestMapping(value = "/patchViewNum", method = RequestMethod.PATCH)
	public @ResponseBody String patchViewNum(@RequestParam Integer videoId, HttpServletResponse res) {
		if (videoService.videoAction(videoId, "view")) {
			return " react success";
		}
		res.setStatus(304);
		return "react failed";
	}

	// 需要token验证
	@RequestMapping(value = "/patchOtherNum", method = RequestMethod.PATCH)
	public @ResponseBody String reactVideo(@RequestParam Integer videoId, @RequestParam String action,
			HttpServletResponse res) {
		if (videoService.videoAction(videoId, action)) {
			return " react success";
		}
		res.setStatus(304);
		return "react failed";
	}

	// 需要token验证
	@RequestMapping(value = "/patchTags", method = RequestMethod.PATCH)
	public @ResponseBody String patchTags(@RequestParam String tagJsonString, @RequestParam Integer videoId,
			HttpServletResponse res) {
		if (videoService.updateTags(videoId, tagJsonString)) {
			return "update tags success";
		}
		res.setStatus(304);
		return "update tags failed";
	}
}
