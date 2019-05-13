package com.empty.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<DanEntity> getVideoDan(Integer videoId) {
        return baseDanMapper.selectDanByVideoId(videoId);
    }

    @Transactional
    @Override
    public boolean saveNewDan(DanEntity newDan) {
        if (newDan == null) {
            return false;
        }
        baseDanMapper.saveNewDan(newDan);
        return true;
    }

}
