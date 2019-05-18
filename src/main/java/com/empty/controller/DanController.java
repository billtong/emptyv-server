package com.empty.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empty.entity.DanEntity;
import com.empty.service.BaseDanService;

@Controller
@RequestMapping(value = "/api/dan", produces = "application/json;charset=UTF-8")
public class DanController {

	@Resource(name = "danService")
	BaseDanService danService;

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public @ResponseBody List<DanEntity> loadDanByVideoId(@RequestParam Integer videoId) {
		return danService.getVideoDan(videoId);
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public @ResponseBody String loadDanByVideoId(@RequestBody DanEntity newDan, HttpServletResponse res) {
		if (!danService.saveNewDan(newDan)) {
			res.setStatus(400);
			return "failed";
		}
		return "success";
	}
}
