package com.empty.mapper;

import java.util.List;

import com.empty.entity.HistoryEntity;
import com.empty.entity.HistoryEntity.histoyActionCode;

public interface HistoryMapper {
	List<HistoryEntity> findByUserId(Integer userId);

	void saveNewHistory(HistoryEntity historyEntity);
	
}
