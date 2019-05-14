package com.empty.dao;

import java.util.List;

import com.empty.entity.HistoryEntity;
import org.apache.ibatis.annotations.Mapper;

public interface HistoryMapper {
    List<HistoryEntity> findByUserId(Integer userId);

    void saveNewHistory(HistoryEntity historyEntity);

}
