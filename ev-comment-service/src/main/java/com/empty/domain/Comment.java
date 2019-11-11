package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "comment")
public class Comment {
    @Id
    private String id;

    @Indexed(name = "video_id")
    private String videoId;

    @Indexed(name = "parent_id")
    private String parentId;

    @Indexed(name = "like_num")
    private Long likeNum = 0L;

    @Indexed(name = "reply_num")
    private Long replyNum = 0L;

    private String userId;

    private String at;              //直接回复的人

    private Date created = new Date();

    private String text;

    private boolean deleted = false;
}
