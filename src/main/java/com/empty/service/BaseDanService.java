package com.empty.service;

import java.util.List;

import com.empty.entity.DanEntity;

public interface BaseDanService {

    List<DanEntity> getVideoDan(Integer videoId);

    boolean saveNewDan(DanEntity newDan);

}
