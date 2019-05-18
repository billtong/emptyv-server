package com.empty.mapper;

import java.util.List;

import com.empty.entity.FavEntity;

public interface FavMapper {

	void saveNewFav(FavEntity newFav);

	List<FavEntity> selectFavsByUserId(Integer userId);

	FavEntity getFavByFavId(Integer favId);

	void updateFav(FavEntity newFav);

	void deleteFav(Integer favId);

}
