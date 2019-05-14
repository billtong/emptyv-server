package com.empty.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.empty.entity.HistoryEntity;
import com.empty.service.HistoryService;

@RestController
@RequestMapping(value = "/api/history", produces = "application/json;charset=UTF-8")
public class HistoryController {

    @Resource(name = "historyService")
    HistoryService historyService;

    @RequestMapping(value = "/getHistory", method = RequestMethod.GET)
    public List<HistoryEntity> getHistory(@RequestParam Integer userId) {
        return historyService.getHistoryList(userId);
    }
}
