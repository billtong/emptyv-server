package com.empty.service;

import com.empty.domain.OperationEnum;
import com.empty.domain.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class VideoCountFactory {
    @Autowired
    VideoCountProduct videoCountProduct;

    public Mono<Video> process(Map<String, Object> countMap) {
        String operation = String.valueOf(countMap.get("operation"));
        switch (OperationEnum.valueOf(operation)) {
            case WRITE_A_COMMENT:
                return videoCountProduct.handleCommentCount(countMap);
            case FAV_A_VIDEO:
                return videoCountProduct.handleFavCount(countMap);
            case WRITE_A_DAN:
                return videoCountProduct.handleDanCount(countMap);
        }
        return Mono.empty();
    }
}
