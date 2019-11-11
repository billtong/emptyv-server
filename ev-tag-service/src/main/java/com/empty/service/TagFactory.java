package com.empty.service;

import com.empty.domain.OperationEnum;
import com.empty.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Component
public class TagFactory {
    @Autowired
    TagProduct tagProduct;

    public Flux<Tag> process(Map<String, Object> tagMap) {
        String operation = String.valueOf(tagMap.get("operation"));
        Map videoMap = (Map) tagMap.get("object");
        String videoId = String.valueOf(videoMap.get("id"));
        switch (OperationEnum.valueOf(operation)) {
            case POST_A_VIDEO:
                List<String> tags = (List<String>) videoMap.get("tags");
                return tagProduct.handleAllTags(videoId, tags);
            case TAG_A_VIDEO:
                String tag = String.valueOf(tagMap.get("tag"));
                return tagProduct.handleOneTag(videoId, tag).flux();
        }
        return Flux.empty();
    }
}
