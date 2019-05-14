package com.empty.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

    @Autowired
    RedisTemplate redisTemplate;

    // 装载上 videoList发送回去
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FavEntity getFavByFavId(Integer favId) {
        String favKey = "fav_" + favId;
        Boolean hasFavKey = redisTemplate.hasKey(favKey);
        ValueOperations<String, FavEntity> operations1 = redisTemplate.opsForValue();
        FavEntity fav = hasFavKey ? operations1.get(favKey) : favMapper.getFavByFavId(favId);
        if(!hasFavKey) {
            operations1.set(favKey, fav,2, TimeUnit.SECONDS);
        }

        String[] videoIds = fav.getFavList().split(",");
        List<VideoEntity> videoList = new ArrayList<>();
        for (String str : videoIds) {
            Integer videoId = Integer.parseInt(str);

            String videoKey = "video_" + videoId;
            Boolean hasVideoKey = redisTemplate.hasKey(videoKey);
            ValueOperations<String, VideoEntity> operations2 = redisTemplate.opsForValue();
            VideoEntity video = hasVideoKey ? operations2.get(videoKey) : videoMapper.findVideoById(videoId);
            if(!hasVideoKey) {
                operations2.set(videoKey, video,2, TimeUnit.SECONDS);
            }
            videoList.add(video);
        }
        fav.setVideoList(videoList);
        return fav;
    }

    // 多个favList
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<FavEntity> getFavsByUserId(Integer userId) {

        String favListKey = "fav_list_"+userId;
        Boolean hasFavListKey = redisTemplate.hasKey(favListKey);
        ValueOperations<String, List<FavEntity>> operations1 = redisTemplate.opsForValue();
        List<FavEntity> favList = hasFavListKey ? operations1.get(favListKey) :favMapper.selectFavsByUserId(userId);
        if(!hasFavListKey) {
            operations1.set(favListKey, favList,2, TimeUnit.SECONDS);
        }
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
                String favKey = "fav_" + newFav.getFavId();
                String favListKey = "fav_list_"+ newFav.getUserId();
                deleteCache(favKey);
                deleteCache(favListKey);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean saveNewFav(FavEntity newFav) {
        if (newFav != null) {
            favMapper.saveNewFav(newFav);
            String favListKey = "fav_list_"+ newFav.getUserId();
            deleteCache(favListKey);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteFavByFavId(Integer favId, Integer userId) {

        String favKey = "fav_" + favId;
        Boolean hasFavKey = redisTemplate.hasKey(favKey);
        ValueOperations<String, FavEntity> operations1 = redisTemplate.opsForValue();
        FavEntity fav = hasFavKey ? operations1.get(favKey) : favMapper.getFavByFavId(favId);
        if(!hasFavKey) {
            operations1.set(favKey, fav,2, TimeUnit.SECONDS);
        }

        if (fav != null && fav.getUserId() == userId) {
            favMapper.deleteFav(favId);
            String favKey2 = "fav_" + favId;
            String favListKey = "fav_list_"+ userId;
            deleteCache(favKey2);
            deleteCache(favListKey);
            return true;
        }
        return false;
    }

    private void deleteCache(String key) {
        if(redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }
}
