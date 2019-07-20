package com.empty;

import com.empty.domain.FavList;
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
import java.util.List;
import java.util.Random;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmptyVideoFavListServiceApplicationTests {
    //add it manully
    private final String bearToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZDMxMTNmZmU0MmNjOTJmNDA3YWVkYTYiLCJyb2xlcyI6ImFkbWluIiwiaXNzIjoiZW1wdHl2aWRlby5jb20iLCJleHAiOjE1NjM2MjA3Nzl9.K5YBQQ9hYCo607B6Gxh4zWsgX7n8Kq0DrtCZzqVsA2U";
    private final String testId = "test";//String.valueOf(new Random().nextInt());
    @Autowired
    RouterFunctionConfig routerFunctionConfig;

    @Test
    public void test1PostRouterFunction() {
        System.out.println(testId);
        FavList favList = new FavList();
        favList.setId(testId);
        favList.setVideoIds(new ArrayList<>());
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authFavListRouterFunction()).build();
        client.post().uri("/api/favlist")
                .body(BodyInserters.fromObject(favList))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void test2PatchRouterFunction() {
        System.out.println(testId);
        FavList favList = new FavList();
        List<String> videos = new ArrayList<>();
        videos.add("a");
        favList.setVideoIds(videos);
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authFavListRouterFunction()).build();
        client.patch().uri("/api/favlist/".concat(testId))
                .body(BodyInserters.fromObject(favList))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test3DeleteFavListRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authFavListRouterFunction()).build();
        client.delete().uri("/api/favlist/".concat(testId))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test4GetFavList() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.getFavListRouterFunction()).build();
        client.get().uri("/api/favlist/".concat(testId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }
}
