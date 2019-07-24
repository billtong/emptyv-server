package com.empty;

import com.empty.domain.Comment;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommentServiceApplicationTests {

    //change it manually
    private final String bearToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZDMxMTNmZmU0MmNjOTJmNDA3YWVkYTYiLCJyb2xlcyI6ImFkbWluIiwiaXNzIjoiZW1wdHl2aWRlby5jb20iLCJleHAiOjE2NTAzMjE1OTN9.oB63ETAIlBRmk8xGMoAJqObvHey4IBcdNT1YXtKP1z0";
    private final String testId = "test";//String.valueOf(new Random().nextInt());

    @Autowired
    RouterFunctionConfig routerFunctionConfig;

    @Test
    public void test1PostRouterWithAtFunction() {
        Comment comment = new Comment();
        comment.setId(testId);
        comment.setText("test comment");
        comment.setVideoId("1");
        comment.setAt("atsomeone");
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.commentWithAuthRouterFunction()).build();
        client.post().uri("/api/comment").body(BodyInserters.fromObject(comment))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void test1PostRouterWithoutAtFunction() {
        Comment comment = new Comment();
        comment.setId(testId + "1");
        comment.setText("test comment");
        comment.setVideoId("1");
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.commentWithAuthRouterFunction()).build();
        client.post().uri("/api/comment").body(BodyInserters.fromObject(comment))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void test2PatchRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.commentWithAuthRouterFunction()).build();
        client.patch().uri("/api/comment/".concat(testId).concat("/like"))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test3DeleteRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.commentWithAuthRouterFunction()).build();
        client.delete().uri("/api/comment/".concat(testId))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .exchange()
                .expectStatus().isOk();
    }
}
