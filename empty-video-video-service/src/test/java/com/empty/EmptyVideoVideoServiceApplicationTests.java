package com.empty;

import com.empty.domain.Video;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.ArrayList;
import java.util.Random;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmptyVideoVideoServiceApplicationTests {

    //add it manully
    private final String bearToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZDMxMTNmZmU0MmNjOTJmNDA3YWVkYTYiLCJyb2xlcyI6ImFkbWluIiwiaXNzIjoiZW1wdHl2aWRlby5jb20iLCJleHAiOjE1NjM2MjA3Nzl9.K5YBQQ9hYCo607B6Gxh4zWsgX7n8Kq0DrtCZzqVsA2U";
    private final String testId = String.valueOf(new Random().nextInt());
    @Autowired
    RouterFunctionConfig routerFunctionConfig;

    @Test
    public void test1postVideo() {
        Video video = new Video();
        video.setTags(new ArrayList<>());
        video.setDescription("test desc");
        video.setVideoSrc("test video src");
        video.setThumbnailSrc("teste thumbnail src");
        video.setName("test video name");
        video.setId(testId);
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.postVideoRouterFunction()).build();
        client.post().uri("/api/video")
                .body(BodyInserters.fromObject(video))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void test2GetVideoById() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.getVideoRouterFunction()).build();
        client.get().uri("/api/video/".concat(testId))
                .exchange().expectStatus().isOk();
    }

    @Test
    public void test2GetRandomVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.getVideoRouterFunction()).build();
        client.get().uri("/api/video/random")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void test3likeVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchVideoRouterFunction()).build();
        client.patch().uri("/api/video/".concat(testId))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test4unlikeVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchVideoRouterFunction()).build();
        client.patch().uri("/api/video/".concat(testId))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }
}
