package com.empty.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.FavEntity;
import com.empty.entity.VideoEntity;
import com.empty.dao.BaseVideoMapper;
import com.empty.dao.FavMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("favService")
public class FavService {

    @Autowired
    FavMapper favMapper;

    @Autowired
    BaseVideoMapper videoMapper;

    // 装载上 videoList发送回去
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FavEntity getFavByFavId(Integer favId) {
        FavEntity fav = favMapper.getFavByFavId(favId);
        String[] videoIds = fav.getFavList().split(",");
        List<VideoEntity> videoList = new ArrayList<>();
        for (String str : videoIds) {
            Integer videoId = Integer.parseInt(str);
            videoList.add(videoMapper.findVideoById(videoId));
        }
        fav.setVideoList(videoList);
        return fav;
    }

    // 多个favList
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<FavEntity> getFavsByUserId(Integer userId) {
        List<FavEntity> favList = favMapper.selectFavsByUserId(userId);

        if (favList != null) {
            for (int i = 0; i < favList.size(); i++) {
                favList.set(i, getFavByFavId(favList.get(i).getFavId()));
            }
        }
        return favList;
    }

    @Transactional
    public boolean updateFav(FavEntity newFav) {
        if (newFav != null) {
            if (newFav.getFavId() != null && favMapper.getFavByFavId(newFav.getFavId()) != null) {
                favMapper.updateFav(newFav);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean saveNewFav(FavEntity newFav) {
        if (newFav != null) {
            favMapper.saveNewFav(newFav);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteFavByFavId(Integer favId, Integer userId) {
        FavEntity fav = favMapper.getFavByFavId(favId);
        if (fav != null && fav.getUserId() == userId) {
            favMapper.deleteFav(favId);
            return true;
        }
        return false;
    }

}
