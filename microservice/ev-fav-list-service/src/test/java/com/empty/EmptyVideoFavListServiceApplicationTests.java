package com.empty;

import com.empty.domain.FavList;
import com.empty.domain.OperationEnum;
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

import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmptyVideoFavListServiceApplicationTests {
    //add it manully
    private final String bearToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZDMxMTNmZmU0MmNjOTJmNDA3YWVkYTYiLCJyb2xlcyI6ImFkbWluIiwiaXNzIjoiZW1wdHl2aWRlby5jb20iLCJleHAiOjE2NTAzMjE1OTN9.oB63ETAIlBRmk8xGMoAJqObvHey4IBcdNT1YXtKP1z0";
    private final String testId = "test";//String.valueOf(new Random().nextInt());
    @Autowired
    RouterFunctionConfig routerFunctionConfig;

    @Test@Ignore
    public void test1PostRouterFunction() {
        FavList favList = new FavList();
        favList.setId(testId);
        favList.setVideoIds(Arrays.asList("test"));
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authFavListRouterFunction()).build();
        client.post().uri("/favlist")
                .body(BodyInserters.fromObject(favList))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test@Ignore
    public void test2PatchRouterFunction() {
        String param = "/?operation=".concat(OperationEnum.FAV_A_VIDEO.toString()).concat("&videoId=test");
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authFavListRouterFunction()).build();
        client.patch().uri("/favlist/".concat(testId).concat(param))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test@Ignore
    public void test4GetFavList() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.getFavListRouterFunction()).build();
        client.get().uri("/favlist/".concat(testId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test@Ignore
    public void test3DeleteFavListRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authFavListRouterFunction()).build();
        client.delete().uri("/favlist/".concat(testId))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk();
    }
}
