package com.empty.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empty.entity.CommentEntity;
import com.empty.entity.HistoryEntity;
import com.empty.service.HistoryService;
import com.empty.service.impl.BaseUseServiceImpl;

@Controller
@RequestMapping(value = "/api/history", produces = "application/json;charset=UTF-8")
public class HistoryController {
	
	@Resource(name = "historyService")
	HistoryService historyService;
	
	@RequestMapping(value = "/getHistory", method = RequestMethod.GET)
	public @ResponseBody List<HistoryEntity> getHistory (@RequestParam Integer userId) {
		return historyService.getHistoryList(userId);
	}

	
}
