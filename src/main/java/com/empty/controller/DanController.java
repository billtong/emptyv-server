package com.empty.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.*;

import com.empty.entity.DanEntity;
import com.empty.service.BaseDanService;

@RestController
@RequestMapping(value = "/api/dan", produces = "application/json;charset=UTF-8")
public class DanController {

    @Resource(name = "danService")
    BaseDanService danService;

    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public List<DanEntity> loadDanByVideoId(@RequestParam Integer videoId) {
        return danService.getVideoDan(videoId);
    }

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public String loadDanByVideoId(@RequestBody DanEntity newDan, HttpServletResponse res) {
        if (!danService.saveNewDan(newDan)) {
            res.setStatus(400);
            return "failed";
        }
        return "success";
    }
}
