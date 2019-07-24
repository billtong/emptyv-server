package com.empty;

import com.empty.domain.OperationEnum;
import com.empty.domain.Video;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
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
public class EmptyVideo2ServiceApplicationTests {

    //add it manully
    private final String bearToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZDMxMTNmZmU0MmNjOTJmNDA3YWVkYTYiLCJyb2xlcyI6ImFkbWluIiwiaXNzIjoiZW1wdHl2aWRlby5jb20iLCJleHAiOjE2NTAzMjE1OTN9.oB63ETAIlBRmk8xGMoAJqObvHey4IBcdNT1YXtKP1z0";
    private final String testId = "test";
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
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
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
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch().uri("/api/video/".concat(testId).concat("/").concat(OperationEnum.LIKE_A_VIDEO.toString()))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test4unlikeVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch().uri("/api/video/".concat(testId).concat("/").concat(OperationEnum.UNLIKE_A_VIDEO.toString()))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test5CancelLikeVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch().uri("/api/video/".concat(testId).concat("/").concat(OperationEnum.CANCEL_LIKE_A_VIDEO.toString()))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test6CancleUnlikeVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch().uri("/api/video/".concat(testId).concat("/").concat(OperationEnum.CANCEL_UNLIKE_A_VIDEO.toString()))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test7TagVideo() {
        String param = "?tag=testtag";
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch()
                .uri("/api/video/".concat(testId).concat("/").concat(OperationEnum.TAG_A_VIDEO.toString()).concat(param))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }


}
