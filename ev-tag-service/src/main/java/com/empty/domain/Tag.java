package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "tag")
public class Tag {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private Date create = new Date();
    private String description;
    private List<String> videoIds = new ArrayList<>();

    public Tag(String name) {
        this();
        this.name = name;
    }

}
