package com.empty.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.empty.service.BaseMessageService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empty.entity.MessageEntity;

@Controller
@RequestMapping(value = "/api/message", produces = "application/json;charset=UTF-8")
public class MessageController {
  
  @Resource(name = "messageService")
  BaseMessageService msgService;

  @RequestMapping(value="/get", method = RequestMethod.GET)
  public @ResponseBody List<MessageEntity> getMsgList(@RequestParam Integer userId) {
    return msgService.getMsgListByUserId(userId);
  }

  @RequestMapping(value="/write", method = RequestMethod.POST)
  public void writeNewMsg(@RequestBody MessageEntity newMe, @RequestParam Integer userId, HttpServletResponse res) {
    if(msgService.saveNewMsg(newMe, userId))
      res.setStatus(200);
    else
      res.setStatus(403);
  }

  @RequestMapping(value = "/edit", method = RequestMethod.PATCH)
  public void editMsg(@RequestBody MessageEntity newMe, @RequestParam Integer userId, HttpServletResponse res) {
    if(msgService.updateMsg(newMe, userId))
      res.setStatus(200);
    else
      res.setStatus(403);
  }

  @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  public void deleteMsg(@RequestParam Integer msgId, @RequestParam Integer userId, HttpServletResponse res) {
    if (msgService.deleteMsg(msgId, userId))
      res.setStatus(200);
    else
      res.setStatus(403);
  }
}