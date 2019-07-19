package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Map<String, String> content;  //some id;\

    public Notification(String to, Map<String, String> notiContent) {
        this.setTo(to);
        this.setContent(notiContent);
    }
}

/*
    {
        "field": "",
        "subject": "userId",
        "action": "did something",
        "object": "commentId/messageId..."
    }
*/
