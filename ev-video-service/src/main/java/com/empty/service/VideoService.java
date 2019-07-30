package com.empty.service;

import com.empty.domain.OperationEnum;
import com.empty.domain.Video;
import com.empty.repository.VideoRepository;
import com.empty.util.ListTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Service
public class VideoService {

    @Autowired
    VideoRepository videoRepository;
    @Autowired
    ListTools listTools;

    public Mono<ServerResponse> getVideoById(ServerRequest serverRequest) {
        String videoId = serverRequest.pathVariable("id");
        return videoRepository.findById(videoId).flatMap(video -> ok().body(Mono.just(video), Video.class));
    }

    public Mono<ServerResponse> getRandomVideos(ServerRequest serverRequest) {
        return videoRepository.findAll().collectList().flatMap(videos -> {
            //listTools.shuffle(videos);
            return ok().body(Mono.just(videos), List.class);
        });
    }

    public Mono<ServerResponse> search(ServerRequest serverRequest) {
        /*
            need elacstic search engine and real data to actually implement this method.
            wait for it.
         */
        return ok().build();
    }

    public Mono<ServerResponse> updateVideo(ServerRequest serverRequest) {
        String videoId = serverRequest.pathVariable("id");
        String operation = serverRequest.queryParam("operation").get();
        Mono<Video> videoMono = videoRepository.findById(videoId);
        return Mono.zip(Mono.just(operation), videoMono, Mono.just(serverRequest)).flatMap(tuple -> {
            String operation2 = tuple.getT1();
            Video video = tuple.getT2();
            ServerRequest serverRequest1 = tuple.getT3();
            switch (OperationEnum.valueOf(operation2)) {
                case LIKE_A_VIDEO:
                    video.setLikeCount(video.getLikeCount() + 1);
                    break;
                case CANCEL_LIKE_A_VIDEO:
                    video.setLikeCount(video.getLikeCount() - 1);
                    break;
                case UNLIKE_A_VIDEO:
                    video.setUnlikeCount(video.getUnlikeCount() + 1);
                    break;
                case CANCEL_UNLIKE_A_VIDEO:
                    video.setUnlikeCount(video.getUnlikeCount() - 1);
                    break;
                case VIEW_A_VIDEO:
                    video.setViewCount(video.getViewCount() + 1);
                    break;
                case TAG_A_VIDEO:
                    String tag = serverRequest1.queryParam("tag").get();
                    video.getTags().add(tag);
                    video.setTags(video.getTags());
                    break;
            }
            return videoRepository.save(video).zipWith(Mono.just(serverRequest1)).flatMap(tuple2 -> {
                Video video1 = tuple2.getT1();
                ServerRequest serverRequest2 = tuple2.getT2();
                serverRequest2.attributes().put("video", video1);
                return Mono.just(video1);
            }).then(ok().build());
        });
    }

    public Mono<ServerResponse> postNewVideo(ServerRequest serverRequest) {
        Mono<Video> newVideoMono = serverRequest.bodyToMono(Video.class);
        return Mono.zip(Mono.just(serverRequest), newVideoMono).flatMap(tuple1 -> {
            String userId = String.valueOf(tuple1.getT1().attributes().get("authId"));
            Video newVideo = tuple1.getT2();
            newVideo.setUserId(userId);
            return videoRepository.save(newVideo).zipWith(Mono.just(tuple1.getT1())).flatMap(tuple2 -> {
                Video videoSaved = tuple2.getT1();
                ServerRequest serverRequest1 = tuple2.getT2();
                serverRequest1.attributes().put("video", videoSaved);
                return Mono.just(videoSaved);
            }).then(status(201).build());
        });
    }
}
