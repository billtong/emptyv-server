package com.empty.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.empty.entity.VideoEntity;
import com.empty.service.BaseVideoService;

@RestController
@RequestMapping(value = "/api/video", produces = "application/json;charset=UTF-8")
public class VideoController {

    @Resource(name = "videoService")
    BaseVideoService videoService;

    @RequestMapping(value = "/getVideo", method = RequestMethod.GET)
    public VideoEntity getVideoById(@RequestParam Integer videoId, HttpServletResponse res) {
        VideoEntity v = videoService.getVideoById(videoId);
        if (v != null) {
            return v;
        }
        res.setStatus(404);
        return null;
    }

    @RequestMapping(value = "/getVideoList", method = RequestMethod.GET)
    public Map<String, Object> searchVideo(@RequestParam String word, @RequestParam String filter,
                                           @RequestParam Integer userId, HttpServletResponse res) {
        return videoService.getVideos(word, filter, userId);
    }

    @RequestMapping(value = "/patchViewNum", method = RequestMethod.PATCH)
    public String patchViewNum(@RequestParam Integer videoId, Integer userId, HttpServletResponse res) {
        if (videoService.videoAction(videoId, "view", userId)) {
            return " react success";
        }
        res.setStatus(304);
        return "react failed";
    }

    @RequestMapping(value = "/patchOtherNum", method = RequestMethod.PATCH)
    public String reactVideo(@RequestParam Integer videoId, @RequestParam Integer userId,
                             @RequestParam String action, HttpServletResponse res) {
        if (videoService.videoAction(videoId, action, userId)) {
            return " react success";
        }
        res.setStatus(304);
        return "react failed";
    }

    @RequestMapping(value = "/patchTags", method = RequestMethod.PATCH)
    public String patchTags(@RequestParam String tagJsonString, @RequestParam Integer videoId,
                            @RequestParam Integer userId, HttpServletResponse res) {
        if (videoService.updateTags(videoId, tagJsonString)) {
            return "update tags success";
        }
        res.setStatus(304);
        return "update tags failed";
    }
}
