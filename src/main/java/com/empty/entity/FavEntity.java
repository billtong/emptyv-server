package com.empty.entity;

import java.util.Date;
import java.util.List;

public class FavEntity {
	private Integer favId;
	private String favName;
	private String favList;
	private Integer userId;
	private Date favDate;
	private boolean favIsPublish;

	// 不放在数据空中，而是根据favList生成的
	private List<VideoEntity> videoList;

	public Integer getFavId() {
		return favId;
	}

	public void setFavId(Integer favId) {
		this.favId = favId;
	}

	public String getFavName() {
		return favName;
	}

	public void setFavName(String favName) {
		this.favName = favName;
	}

	public String getFavList() {
		return favList;
	}

	public void setFavList(String favList) {
		this.favList = favList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getFavDate() {
		return favDate;
	}

	public void setFavDate(Date favDate) {
		this.favDate = favDate;
	}

	public boolean isFavIsPublish() {
		return favIsPublish;
	}

	public void setFavIsPublish(boolean favIsPublish) {
		this.favIsPublish = favIsPublish;
	}

	public List<VideoEntity> getVideoList() {
		return videoList;
	}

	public void setVideoList(List<VideoEntity> videoList) {
		this.videoList = videoList;
	}
}
