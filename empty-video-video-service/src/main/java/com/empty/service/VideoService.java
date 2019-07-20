package com.empty.service;

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
            listTools.shuffle(videos);
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
        String action = String.valueOf(serverRequest.queryParam("action"));
        Mono<Video> videoMono = videoRepository.findById(videoId);
        return Mono.zip(Mono.just(action), videoMono).flatMap(tuple -> {
            String action1 = tuple.getT1();
            Video video = tuple.getT2();
            switch (action1) {
                case "like":
                    video.setLikeCount(video.getLikeCount() + 1);
                    break;
                case "unlike":
                    video.setUnlikeCount(video.getUnlikeCount() + 1);
                    break;
                case "view":
                    video.setViewCount(video.getViewCount() + 1);
            }
            return videoRepository.save(video).then(ok().build());
        });
    }

    public Mono<ServerResponse> postNewVideo(ServerRequest serverRequest) {
        Mono<Video> newVideoMono = serverRequest.bodyToMono(Video.class);
        return Mono.zip(Mono.just(serverRequest), newVideoMono).flatMap(tuple1 -> {
            String userId = String.valueOf(tuple1.getT1().attributes().get("authId"));
            Video newVideo = tuple1.getT2();
            newVideo.setUserId(userId);
            return videoRepository.save(newVideo).then(status(201).build());
        });
    }
}
