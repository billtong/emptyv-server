package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "video")
public class Video {
    @Id
    private String id;
    private String name;
    @Indexed
    private Date create = new Date();
    @Indexed
    private String userId;
    private String videoSrc;
    private String thumbnailSrc;
    private String description;

    private List<String> tags; //<tag name>

    //count
    private long viewCount = 0;
    private long likeCount = 0;
    private long unlikeCount = 0;
    private long favCount = 0;
    private long danCount = 0;
    private long commentCount = 0;
}
