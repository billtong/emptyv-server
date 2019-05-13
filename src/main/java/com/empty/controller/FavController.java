package com.empty.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.empty.entity.FavEntity;
import com.empty.service.FavService;

@RestController
@RequestMapping(value = "/api/fav", produces = "application/json;charset=UTF-8")
public class FavController {

    @Resource(name = "favService")
    FavService favService;

    // 根据favList里的id值动态生成videoLis一起传回来
    @RequestMapping(value = "getFavById", method = RequestMethod.GET)
    public FavEntity getFavById(@RequestParam Integer favId, HttpServletResponse res) {
        FavEntity fav = favService.getFavByFavId(favId);
        if (fav == null) {
            res.setStatus(404);
        }
        return fav;
    }

    @RequestMapping(value = "getFavsByUserId", method = RequestMethod.GET)
    public List<FavEntity> getFavsByUserId(@RequestParam Integer userId, HttpServletResponse res) {
        List<FavEntity> favList = favService.getFavsByUserId(userId);
        if (favList == null) {
            res.setStatus(404);
        }
        return favList;
    }

    @RequestMapping(value = "patchFav", method = RequestMethod.PATCH)
    public String patchVideos(@RequestBody FavEntity newFav, @RequestParam Integer userId,
                       HttpServletResponse res) {
        if (userId == newFav.getUserId() && favService.updateFav(newFav)) {
            return "success";
        } else {
            res.setStatus(404);
            return "failed";
        }
    }

    /*
     * body { "favName" : "SSSS", "favList" : "2,1,4", "userId" : 1, "favIsPublish":
     * 0 }
     */
    @RequestMapping(value = "postNewFav", method = RequestMethod.POST)
    public String postNewFav(@RequestBody FavEntity newFav, @RequestParam Integer userId,
                      HttpServletResponse res) {
        if (userId == newFav.getUserId() && favService.saveNewFav(newFav)) {
            return "success";
        } else {
            res.setStatus(400);
            return "failed";
        }
    }

    @RequestMapping(value = "deleteFav", method = RequestMethod.DELETE)
    public String deleteFav(@RequestParam Integer favId, @RequestParam Integer userId,
                     HttpServletResponse res) {
        if (favService.deleteFavByFavId(favId, userId)) {
            return "success";
        } else {
            res.setStatus(400);
            return "failed";
        }
    }
}
