package com.empty.service;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empty.entity.FavEntity;
import com.empty.entity.VideoEntity;
import com.empty.mapper.BaseVideoMapper;
import com.empty.mapper.FavMapper;

@Service("favService")
public class FavService {

	@Autowired
	FavMapper favMapper;

	@Autowired
	BaseVideoMapper videoMapper;

	// 装载上 videoList发送回去
	public FavEntity getFavByFavId(Integer favId) {
		FavEntity fav = favMapper.getFavByFavId(favId);
		String[] videoIds = fav.getFavList().split(",");
		List<VideoEntity> videoList = new ArrayList<>();
		for (String str : videoIds) {
			System.out.println(str);
			Integer videoId = Integer.parseInt(str);
			videoList.add(videoMapper.findVideoById(videoId));
		}
		fav.setVideoList(videoList);
		return fav;
	}

	// 多个favList
	public List<FavEntity> getFavsByUserId(Integer userId) {
		List<FavEntity> favList = favMapper.selectFavsByUserId(userId);

		if (favList != null) {
			for (int i = 0; i < favList.size(); i++) {
				favList.set(i, getFavByFavId(favList.get(i).getFavId()));
			}
		}
		return favList;
	}

	public boolean updateFav(FavEntity newFav) {
		if(newFav != null) {
			if(newFav.getFavId()!=null && favMapper.getFavByFavId(newFav.getFavId()) != null) {
				favMapper.updateFav(newFav);
				return true;	
			}
		}
		return false;
	}

	public boolean saveNewFav(FavEntity newFav) {
		if(newFav != null) {
			favMapper.saveNewFav(newFav);
			return true;
		}
		return false;
	}

	public boolean deleteFavByFavId(Integer favId) {
		if(favMapper.getFavByFavId(favId) != null) {
			favMapper.deleteFav(favId);
			return true;
		}
		return false;
	}

	
}
