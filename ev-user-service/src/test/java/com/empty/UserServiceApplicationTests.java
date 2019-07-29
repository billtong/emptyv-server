package com.empty;

import com.empty.domain.User;
import com.empty.repository.SessionRepository;
import com.empty.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private RouterFunctionConfig routerFunctionConfig;

    private String sessionId;
    private String userId;

    @Test
    public void test1UserRouterFunction() {
        Map<String, String> newUserForm = new HashMap<>();
        newUserForm.put("email", "zhiyuantongbill@gmail.com");
        newUserForm.put("name", "billtong");
        newUserForm.put("pwd", "123456");
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.userRouterFunction()).build();
        client.post()
                .uri("/api/user")
                .body(BodyInserters.fromObject(newUserForm))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @Ignore //这里的sessionId目前没有获取的方法，因此暂时无法测试,需要学习reactor test模块
    public void test2AuthActiveRouterFunction() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authRouterFunction()).build();
        client.get().uri("/auth/active/".concat(sessionId))
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(("zhiyuantongbill@gamil.com:123456").getBytes()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void test3AuthTokenRouterFunction() {
        //login -> check token
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authRouterFunction()).build();
        client.post()
                .uri("/auth/login")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(("test:123456").getBytes()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String bearToken = response.getResponseHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            client.get().uri("/auth/user")
                    .header(HttpHeaders.AUTHORIZATION, bearToken)
                    .exchange().expectStatus().isOk();
        });
    }

    @Test
    @Ignore //user Id 暂时无法获得。。。
    public void test4UserPatchRouterFunction() {
        //login -> check token
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.authRouterFunction()).build();
        client.post()
                .uri("/auth/login")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(("test:123456").getBytes()))
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
            String bearToken = response.getResponseHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            User u = new User();
            client.patch().uri("/api/user")
                    .body(BodyInserters.fromObject(u))
                    .header(HttpHeaders.AUTHORIZATION, bearToken)
                    .exchange().expectStatus().isOk();
        });
    }
}
