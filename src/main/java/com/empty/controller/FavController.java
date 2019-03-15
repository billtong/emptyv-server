package com.empty.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empty.entity.FavEntity;
import com.empty.service.FavService;

@Controller
@RequestMapping(value = "/api/fav", produces = "application/json;charset=UTF-8")
public class FavController {

	@Resource(name = "favService")
	FavService favService;

	@RequestMapping(value = "getFavById", method = RequestMethod.GET)
	public @ResponseBody FavEntity getFavById(@RequestParam Integer favId, HttpServletResponse res) {
		FavEntity fav = favService.getFavByFavId(favId);
		if (fav == null) {
			res.setStatus(404);
		}
		return fav;
	}

	@RequestMapping(value = "getFavsByUserId", method = RequestMethod.GET)
	public @ResponseBody List<FavEntity> getFavsByUserId(@RequestParam Integer userId, HttpServletResponse res) {
		List<FavEntity> favList = favService.getFavsByUserId(userId);
		if (favList == null) {
			res.setStatus(404);
		}
		return favList;
	}

	@RequestMapping(value = "patchFav", method = RequestMethod.PATCH)
	public @ResponseBody String patchVideos(@RequestBody FavEntity newFav, HttpServletResponse res) {
		if (favService.updateFav(newFav)) {
			return "success";
		} else {
			res.setStatus(404);
			return "failed";
		}
	}

	/*
	body
	{
	"favName" : "SSSS",
	"favList" : "2,1,4",
	"userId" : 1,
	"favIsPublish": 0
	}
	 */
	@RequestMapping(value = "postNewFav", method = RequestMethod.POST)
	public @ResponseBody String postNewFav(@RequestBody FavEntity newFav, HttpServletResponse res) {
		if (favService.saveNewFav(newFav)) {
			return "success";
		} else {
			res.setStatus(400);
			return "failed";
		}
	}
	
	@RequestMapping(value = "deleteFav", method = RequestMethod.DELETE)
	public @ResponseBody String postNewFav(@RequestParam Integer favId, HttpServletResponse res) {
		if (favService.deleteFavByFavId(favId)) {
			return "success";
		} else {
			res.setStatus(400);
			return "failed";
		}
	}
	
	
	
}
