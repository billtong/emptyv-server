CREATE DATABASE empty_db;
USE empty_db;

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
        )    

CREATE TABLE `comment` (                                            
           `comment_id` int(10) unsigned NOT NULL AUTO_INCREMENT,            
           `comment_parent_id` int(10) unsigned NOT NULL DEFAULT '0',        
           `comment_content` varchar(255) NOT NULL,                          
           `comment_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,      
           `video_id` int(10) unsigned NOT NULL,                             
           `user_id` int(10) unsigned DEFAULT NULL,                          
           PRIMARY KEY (`comment_id`)                                        
         ) 


user    CREATE TABLE `user` (                                                                
          `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,                                
          `user_name` varchar(50) NOT NULL,                                                  
          `user_password` varchar(50) NOT NULL,                                              
          `user_email` varchar(50) NOT NULL,                                                 
          `user_perm` varchar(50) DEFAULT NULL,                                              
          `user_reg_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                      
          `user_icon` varchar(50) DEFAULT NULL,                                              
          `user_activated_state` tinyint(4) NOT NULL DEFAULT '0',                            
          PRIMARY KEY (`user_id`)                                                            
        ) 