package com.empty.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.DanEntity;
import com.empty.dao.BaseDanMapper;
import com.empty.service.BaseDanService;

@Service("danService")
public class BaseDanServiceImpl implements BaseDanService {

    @Autowired
    BaseDanMapper baseDanMapper;

    @Override
    public List<DanEntity> getVideoDan(Integer videoId) {
        return baseDanMapper.selectDanByVideoId(videoId);
    }

    @Override
    public boolean saveNewDan(DanEntity newDan) {
        if (newDan == null) {
            return false;
        }
        baseDanMapper.saveNewDan(newDan);
        return true;
    }

}
