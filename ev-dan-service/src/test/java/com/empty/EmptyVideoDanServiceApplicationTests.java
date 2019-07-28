package com.empty;

import com.empty.domain.Dan;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Random;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmptyVideoDanServiceApplicationTests {
    // get token manually
    private final String bearToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZDMxMTNmZmU0MmNjOTJmNDA3YWVkYTYiLCJyb2xlcyI6ImFkbWluIiwiaXNzIjoiZW1wdHl2aWRlby5jb20iLCJleHAiOjE2NTAzMjE1OTN9.oB63ETAIlBRmk8xGMoAJqObvHey4IBcdNT1YXtKP1z0";
    @Autowired
    private RouterFunctionConfig routerFunctionConfig;

    @Test
    @Ignore
    public void test0writeoNEDanRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.postDanRouterFunction()).build();
        Dan dan = new Dan();
        dan.setStyle("normal");
        dan.setText("test dan");
        dan.setVideoId("test");
        dan.setVideoTime(1l);
        client.post().uri("/api/dan")
                .body(BodyInserters.fromObject(dan))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void test1writeDanRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.postDanRouterFunction()).build();
        for (int i = 0; i < 100; i++) {
            Dan dan = new Dan();
            dan.setStyle("normal");
            dan.setText("test dan");
            dan.setVideoId("test");
            dan.setVideoTime(new Random().nextLong());
            client.post().uri("/api/dan")
                    .body(BodyInserters.fromObject(dan))
                    .header(HttpHeaders.AUTHORIZATION, bearToken)
                    .exchange()
                    .expectStatus().isCreated();
        }
    }

    @Test
    public void test2writeAuthErrorRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.postDanRouterFunction()).build();
        Dan dan = new Dan();
        dan.setStyle("normal");
        dan.setText("test dan");
        dan.setVideoId("ev1");
        dan.setVideoTime(new Random().nextLong());
        client.post().uri("/api/dan")
                .body(BodyInserters.fromObject(dan))
                .header(HttpHeaders.AUTHORIZATION, "Bearer asdfasdfasdfasdfs")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    public void test3getDanByVideoIdRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.getDanRouterFunction()).build();
        client.get().uri("/api/dan/test").exchange().returnResult(Dan.class).getResponseBody().subscribe(dan -> {
            log.info("dan time: {}", dan.getVideoTime());
        });
    }

}
