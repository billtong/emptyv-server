package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "notification")
public class Notification implements Serializable {
    private static final long serialVersionUID = 6209167980776428373L;

    @Id
    private String id;
    @Indexed
    private String to;  //user_id
    @Indexed
    private Date created = new Date(); //created time
    private Map<String, Object> content;  //some id;\
}
