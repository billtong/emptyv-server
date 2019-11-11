package com.empty;

import com.empty.domain.User;
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

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceApplicationTests {
    private final String bearToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1ZDMxMTNmZmU0MmNjOTJmNDA3YWVkYTYiLCJyb2xlcyI6ImFkbWluIiwiaXNzIjoiZW1wdHl2aWRlby5jb20iLCJleHAiOjE2NTAzMjE1OTN9.oB63ETAIlBRmk8xGMoAJqObvHey4IBcdNT1YXtKP1z0";
    private final String testId = "test";//String.valueOf(new Random().nextInt());
    @Autowired
    private RouterFunctionConfig routerFunctionConfig;

    @Test
    @Ignore
    public void test1UserRouterFunction() {
        Map<String, String> newUserForm = new HashMap<>();
        newUserForm.put("email", "zhiyuantongbill@gmail.com");
        newUserForm.put("name", "billtong");
        newUserForm.put("pwd", "123456");
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.userRouterFunction()).build();
        client.post()
                .uri("/user")
                .body(BodyInserters.fromObject(newUserForm))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @Ignore //couldn't get the sessionId
    public void test2AuthActiveRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authRouterFunction()).build();
        client.get().uri("/auth/active/".concat(""))
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(("zhiyuantongbill@gamil.com:123456").getBytes()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test2LoginRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authRouterFunction()).build();
        client.post()
                .uri("/auth/login")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(("zhiyuantongbill@gamil.com:123456").getBytes()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Ignore
    public void test3AuthTokenRouterFunction() {
        //login -> check token
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authMiddlewareRouterFunction()).build();
        client.get().uri("/auth/user")
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .exchange().expectStatus().isOk();
    }

    @Ignore
    public void test4UserPatchRouterFunction() {
        //login -> check token
        User u = new User();
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.userPatchUserFunction()).build();
        client.patch().uri("/user")
                .body(BodyInserters.fromObject(u))
                .header(HttpHeaders.AUTHORIZATION, bearToken)
                .exchange().expectStatus().isOk();
    }
}
