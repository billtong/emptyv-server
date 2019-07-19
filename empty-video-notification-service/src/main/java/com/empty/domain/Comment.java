package com.empty.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
public class Comment {
    private String id;
    private String videoId;
    private String parentId;
    private Long likeNum;
    private Long replyNum;
    private String userId;
    private String at;
    private Date created;
    private String text;
    private boolean deleted;
}
