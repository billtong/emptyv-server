package com.agateram.emptyvideo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "oauth_client")
public class OAuthClient {
    @Id
    private String id;  //clientId;
    @Indexed
    private String secret;
    @Indexed
    private String redirectUri;
    //(optional) might add these feature in the future
    private String responseType = "code";
    private Collection<String> scope = Collections.singletonList("user-service");
}
