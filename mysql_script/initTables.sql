CREATE DATABASE `empty_db` CHARACTER SET utf8 COLLATE utf8_general_ci;
use empty_db
CREATE TABLE `video` (                                                                                                                
	`video_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,                                                                                
	`video_name` VARCHAR(50) NOT NULL,                                                                                                  
	`video_src` VARCHAR(255) NOT NULL,                                                                                                  
	`video_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`video_badge` VARCHAR(50) DEFAULT NULL,                                                                          
	`user_id` INT(10) NOT NULL,                                                                                                         
	`video_desc` VARCHAR(255) DEFAULT NULL,                                                                                             
	`video_tag` VARCHAR(255) DEFAULT NULL,
	`video_thumbnail_img` VARCHAR(255) NOT NULL DEFAULT 'http://178.128.236.114:8080/empty-video-files/images/video-thumbnails/0_default.jpg',                                                                                      
	`video_view_num` VARCHAR(15) NOT NULL DEFAULT '0',                                                                                  
	`video_like_num` VARCHAR(15) NOT NULL DEFAULT '0',
	`video_unlike_num` VARCHAR(15) NOT NULL DEFAULT '0',
	`video_favourite_num` VARCHAR(15) NOT NULL DEFAULT '0',
	`video_comment_num` VARCHAR(15) NOT NULL DEFAULT '0',
	`video_danmu_num` VARCHAR(15) NOT NULL DEFAULT '0',
	PRIMARY KEY (`video_id`)                                                                                                            
);
CREATE TABLE `comment` (
	`comment_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`comment_parent_id` INT(10) UNSIGNED NOT NULL DEFAULT '0',
	`comment_content` VARCHAR(255) NOT NULL,
	`comment_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`video_id` INT(10) UNSIGNED NOT NULL,
	`user_id` INT(10) UNSIGNED DEFAULT NULL,
	PRIMARY KEY (`comment_id`)
);
CREATE TABLE `user` (
  `user_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(50) NOT NULL,
  `user_password` VARCHAR(50) NOT NULL,
  `user_email` VARCHAR(50) NOT NULL,
  `user_perm` VARCHAR(50) DEFAULT NULL,
  `user_reg_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_icon` VARCHAR(255) DEFAULT NULL,
  `user_banner` VARCHAR(255) DEFAULT NULL,
  `user_desc` VARCHAR(255) DEFAULT NULL,
  `user_loc` VARCHAR(50) DEFAULT NULL,
  `user_site` VARCHAR(255) DEFAULT NULL,
  `user_level` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `user_achi` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
);
CREATE TABLE `fav`(
	`fav_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
	`fav_name` VARCHAR(50) NOT NULL,
	`fav_list` VARCHAR(255) NOT NULL,
	`user_id` INT UNSIGNED NOT NULL,
	`fav_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`fav_is_publish` BOOLEAN NOT NULL DEFAULT TRUE,
	PRIMARY KEY (`fav_id`)
);
CREATE TABLE `history`(
    `history_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `history_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `action` VARCHAR(1) NOT NULL,
    `user_id` INT UNSIGNED NOT NULL,
    `video_id` INT UNSIGNED,
    `comment_id` INT UNSIGNED,
    PRIMARY KEY (`history_id`)
);
CREATE TABLE `dan` (
  `dan_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dan_send_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dan_curr_time` DOUBLE UNSIGNED NOT NULL DEFAULT '0',
  `dan_content` VARCHAR(100) NOT NULL,
  `dan_style` VARCHAR(255) NOT NULL,
  `video_id` INT(10) UNSIGNED NOT NULL,
  `user_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`dan_id`)
);
CREATE TABLE `message` (
  `msg_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `msg_type` VARCHAR(20) NOT NULL DEFAULT '"text"',
  `msg_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `msg_content` VARCHAR(255) NOT NULL,
  `sender_id` INT(10) UNSIGNED NOT NULL,
  `listener_id` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`msg_id`)
);
COMMIT;



