package com.empty.mapper;

import java.util.List;

import com.empty.entity.DanEntity;

public interface BaseDanMapper {

	List<DanEntity> selectDanByVideoId(Integer videoId);
	
	void saveNewDan(DanEntity dan);
	
}
