package com.empty.entity;

import java.util.Date;

public class UserEntity {
	private Integer userId;
	private String userName;
	private String userPassword;
	private String userEmail;
	private String userPerm;
	private Date userRegDate;
	private String userIcon;
	private String userBanner;
	private String userDesc;
	private String userLoc;
	private String userSite;
	private Integer userLevel;	//user level
	private String userAchi;	//user achievement

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPerm() {
		return userPerm;
	}

	public void setUserPerm(String userPerm) {
		this.userPerm = userPerm;
	}

	public Date getUserRegDate() {
		return userRegDate;
	}

	public void setUserRegDate(Date userRegDate) {
		this.userRegDate = userRegDate;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getUserBanner() {
		return userBanner;
	}

	public void setUserBanner(String userBanner) {
		this.userBanner = userBanner;
	}

	public String getUserDesc() {
		return userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public String getUserLoc() {
		return userLoc;
	}

	public void setUserLoc(String userLoc) {
		this.userLoc = userLoc;
	}

	public String getUserSite() {
		return userSite;
	}

	public void setUserSite(String userSite) {
		this.userSite = userSite;
	}

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	public String getUserAchi() {
		return userAchi;
	}

	public void setUserAchi(String userAchi) {
		this.userAchi = userAchi;
	}
}
