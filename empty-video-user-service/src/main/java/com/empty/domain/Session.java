package com.empty.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.server.WebSession;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "session")
public class Session implements Serializable {
    private static final long serialVersionUID = -1334968245274030578L;
    @Id
    private String id;

    @Indexed(name = "user_id", unique = true)
    private String userId;

    private Date accessed;
    private Date created;
    private Duration interval;
    private Map<String, Object> attr;

    public Session(WebSession webSession, String userId) {
        this.setId(webSession.getId());
        this.setUserId(userId);
        this.setCreated(Date.from(webSession.getCreationTime()));
        this.setAccessed(Date.from(webSession.getLastAccessTime()));
        this.setInterval(webSession.getMaxIdleTime());
        this.setAttr(webSession.getAttributes());
    }
}
