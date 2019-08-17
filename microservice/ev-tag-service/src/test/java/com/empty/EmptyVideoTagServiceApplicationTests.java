package com.empty;

import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmptyVideoTagServiceApplicationTests {

    private final String name = "test";
    @Autowired
    private RouterFunctionConfig routerFunctionConfig;

    @Test@Ignore
    public void test2GetByName() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.getTagRouterFunction()).build();
        client.get().uri("/api/tag/".concat(name))
                .exchange().expectStatus().isOk();
    }

    @Test@Ignore
    public void test2GetRandomVideo() {
        WebTestClient client = WebTestClient.bindToRouterFunction(routerFunctionConfig.getTagRouterFunction()).build();
        client.get().uri("/api/tags/all")
                .exchange().expectStatus().isOk();
    }
}
