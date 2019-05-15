CREATE DATABASE `empty_db` CHARACTER SET utf8 COLLATE utf8_general_ci;
use empty_db;
CREATE TABLE `video` (                                                                                                                
	`video_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,                                                                                
	`video_name` VARCHAR(50) NOT NULL,                                                                                                  
	`video_src` VARCHAR(255) NOT NULL,                                                                                                  
	`video_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`video_badge` VARCHAR(50) DEFAULT NULL,                                                                          
	`user_id` INT(10) NOT NULL,                                                                                                         
	`video_desc` VARCHAR(255) DEFAULT NULL,                                                                                             
	`video_tag` VARCHAR(255) DEFAULT NULL,
	`video_thumbnail_img` VARCHAR(255) NOT NULL DEFAULT 'https://video.safalnews.com/wp-content/plugins/video-thumbnails/default.jpg',
	`video_preview_bg` VARCHAR(255) DEFAULT NULL,                                                                                              
	`video_view_num` VARCHAR(15) NOT NULL DEFAULT '0',                                                                                  
	`video_like_num` VARCHAR(15) NOT NULL DEFAULT '0',
	`video_unlike_num` VARCHAR(15) NOT NULL DEFAULT '0',
	`video_favourite_num` VARCHAR(15) NOT NULL DEFAULT '0',
	`video_comment_num` VARCHAR(15) NOT NULL DEFAULT '0',
	`video_danmu_num` VARCHAR(15) NOT NULL DEFAULT '0',
	PRIMARY KEY (`video_id`)                                                                                                            
)DEFAULT CHARSET=utf8 ;    

CREATE TABLE `comment` (                                            
	`comment_id` int(10) unsigned NOT NULL AUTO_INCREMENT,            
	`comment_parent_id` int(10) unsigned NOT NULL DEFAULT '0',        
	`comment_content` varchar(255) NOT NULL,                          
	`comment_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,      
	`video_id` int(10) unsigned NOT NULL,                             
	`user_id` int(10) unsigned DEFAULT NULL,                          
	PRIMARY KEY (`comment_id`)                                        
)DEFAULT CHARSET=utf8 ;


CREATE TABLE `user` (                                                                
	`user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,                                
	`user_name` varchar(50) NOT NULL,                                                  
	`user_password` varchar(50) NOT NULL,                                              
	`user_email` varchar(50) NOT NULL,                                                 
	`user_perm` varchar(50) DEFAULT NULL,                                              
	`user_reg_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                      
	`user_icon` varchar(50) DEFAULT NULL,                                              
	`user_activated_state` tinyint(4) NOT NULL DEFAULT '1',                            
	PRIMARY KEY (`user_id`)                                                            
)DEFAULT CHARSET=utf8;
        
        
CREATE TABLE `fav`(
	`fav_id` INT UNSIGNED NOT NULL AUTO_INCREMENT, 
	`fav_name` VARCHAR(50) NOT NULL, 
	`fav_list` VARCHAR(255) NOT NULL, 
	`user_id` INT UNSIGNED NOT NULL, 
	`fav_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	`fav_is_publish` BOOLEAN NOT NULL DEFAULT TRUE, 
	PRIMARY KEY (`fav_id`) 
)DEFAULT CHARSET=utf8;
    
CREATE TABLE `history`( 
    `history_id` INT UNSIGNED NOT NULL AUTO_INCREMENT, 
    `history_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    `action` VARCHAR(1) NOT NULL, 
    `user_id` INT UNSIGNED NOT NULL, 
    `video_id` INT UNSIGNED,
    `comment_id` INT UNSIGNED, 
    PRIMARY KEY (`history_id`) 
)DEFAULT CHARSET=utf8;



