package com.empty;

import com.empty.domain.OperationEnum;
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

import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmptyVideo2ServiceApplicationTests {
    private final String bearToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZDMxMTNmZmU0MmNjOTJmNDA3YWVkYTYiLCJyb2xlcyI6ImFkbWluIiwiaXNzIjoiZW1wdHl2aWRlby5jb20iLCJleHAiOjE2NTAzMjE1OTN9.oB63ETAIlBRmk8xGMoAJqObvHey4IBcdNT1YXtKP1z0";
    private final String testId = "test";
    @Autowired
    RouterFunctionConfig routerFunctionConfig;

    @Test
    public void test1postVideo() {
        Video video = new Video();
        video.setTags(Arrays.asList("test1", "test2", "test3"));
        video.setDescription("test desc");
        video.setVideoSrc("test video src");
        video.setThumbnailSrc("test thumbnail src");
        video.setName("test video name");
        video.setId(testId);
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.post().uri("/video")
                .body(BodyInserters.fromObject(video))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void test2GetVideoById() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.getVideoRouterFunction()).build();
        client.get().uri("/video/".concat(testId))
                .exchange().expectStatus().isOk();
    }

    @Test
    public void test2GetRandomVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.getVideoRouterFunction()).build();
        client.get().uri("/videos/random")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void test3likeVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch().uri("/video/".concat(testId).concat("/?operation=").concat(OperationEnum.LIKE_A_VIDEO.toString()))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test4unlikeVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch().uri("/video/".concat(testId).concat("/?operation=").concat(OperationEnum.UNLIKE_A_VIDEO.toString()))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test5CancelLikeVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch().uri("/video/".concat(testId).concat("/?operation=").concat(OperationEnum.CANCEL_LIKE_A_VIDEO.toString()))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test6CancelUnlikeVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch().uri("/video/".concat(testId).concat("/?operation=").concat(OperationEnum.CANCEL_UNLIKE_A_VIDEO.toString()))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test7PatchTagVideo() {
        String param = "&tag=aaa";
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.patchAndPostVideoRouterFunction()).build();
        client.patch()
                .uri("/video/".concat(testId).concat("/?operation=").concat(OperationEnum.TAG_A_VIDEO.toString()).concat(param))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }
}
