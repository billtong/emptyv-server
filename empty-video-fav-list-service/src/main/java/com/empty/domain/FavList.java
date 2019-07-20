package com.empty.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "video_list")
public class FavList {
    @Id
    private String id;
    private String name;
    private String userId;
    private Date created = new Date();
    private boolean isPublic = true;
    private String description;
    private List<String> videoIds;
}
