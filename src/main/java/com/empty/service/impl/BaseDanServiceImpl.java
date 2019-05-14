package com.empty.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.empty.entity.DanEntity;
import com.empty.dao.BaseDanMapper;
import com.empty.service.BaseDanService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("danService")
public class BaseDanServiceImpl implements BaseDanService {

    @Autowired
    BaseDanMapper baseDanMapper;

    //cache the danEntity list for each videoId
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<DanEntity> getVideoDan(Integer videoId) {
        String danListKey = "dan_list_" + videoId;
        ValueOperations<String, List<DanEntity>> operations = redisTemplate.opsForValue();
        Boolean hasDanListKey = redisTemplate.hasKey(danListKey);
        List<DanEntity> danList = hasDanListKey ? operations.get(danListKey) : baseDanMapper.selectDanByVideoId(videoId);
        if(!hasDanListKey) {
            operations.set(danListKey, danList,2, TimeUnit.SECONDS);
        }
        if(danList.size() == 0) {
            return null;
        }
        return danList;
    }

    @Transactional
    @Override
    public boolean saveNewDan(DanEntity newDan) {
        if (newDan == null) {
            return false;
        }
        baseDanMapper.saveNewDan(newDan);
        String key = "dan_list_" + newDan.getVideoId();
        Boolean hasKey = redisTemplate.hasKey(key);
        if(hasKey) {
            redisTemplate.delete(key);
        }
        return true;
    }

}
