package com.empty.service;

import com.empty.domain.Video;
import com.empty.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class VideoCountProduct {
    @Autowired
    VideoRepository videoRepository;

    public Mono<Video> handleCommentCount(Map<String, Object> countMap) {
        Map commentMap = (Map) countMap.get("object");
        String videoId = String.valueOf(commentMap.get("videoId"));
        return videoRepository.findById(videoId).doOnSuccess(video -> {
            video.setCommentCount(video.getCommentCount() + 1);
        });
    }

    public Mono<Video> handleFavCount(Map<String, Object> countMap) {
        String videoId = String.valueOf(countMap.get("videoId"));
        return videoRepository.findById(videoId).doOnSuccess(video -> {
            video.setFavCount(video.getFavCount() + 1);
        });
    }

    public Mono<Video> handleDanCount(Map<String, Object> countMap) {
        Map danMap = (Map) countMap.get("object");
        String videoId = String.valueOf(danMap.get("videoId"));
        return videoRepository.findById(videoId).doOnSuccess(video -> {
            video.setDanCount(video.getDanCount() + 1);
        });
    }
}
