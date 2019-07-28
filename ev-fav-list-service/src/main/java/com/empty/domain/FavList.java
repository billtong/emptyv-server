package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "fav_list")
public class FavList {
    @Id
    private String id;
    private String name;
    private String userId;
    private Date created = new Date();
    private Date updated;   //last update time
    private boolean isPublic = true;
    private String description;
    private List<String> videoIds = new ArrayList<>();
    private boolean deleted = false;
}
