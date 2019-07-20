package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "dan")
public class Dan {
    @Id
    private String id;

    @Indexed
    private Date created = new Date();

    @Indexed
    private String videoId;

    @Indexed
    private Long videoTime;
    private String text;
    private String style;
    private String userId;
}
