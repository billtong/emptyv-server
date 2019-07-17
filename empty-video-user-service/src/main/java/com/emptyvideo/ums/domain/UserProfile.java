package com.emptyvideo.ums.domain;

import lombok.Data;

@Data
public class UserProfile {
    private String name;                //name
    private String avatar;              //avatar url
    private String banner;              //banner url
    private String description;         //description of user
    private String location;            //location
    private String website;             //user url
}
