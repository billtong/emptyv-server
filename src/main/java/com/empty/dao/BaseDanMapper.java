package com.empty.dao;

import java.util.List;

import com.empty.entity.DanEntity;
import org.apache.ibatis.annotations.Mapper;

public interface BaseDanMapper {

    List<DanEntity> selectDanByVideoId(Integer videoId);

    void saveNewDan(DanEntity dan);

}
