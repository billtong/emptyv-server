package com.empty;

import com.empty.service.DanService;
import com.empty.web.HandleFilterFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class EmptyVideoDanServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmptyVideoDanServiceApplication.class, args);
    }
}

@Configuration
class RouterFunctionConfig {
    @Autowired
    DanService danService;
    @Autowired
    HandleFilterFunction handleFilterFunction;

    @Bean
    public RouterFunction<ServerResponse> getDanRouterFunction() {
        return route(GET("/api/dan/{videoId}"), danService::getDanFluxByVideoId);
    }

    @Bean
    public RouterFunction<ServerResponse> postDanRouterFunction() {
        return route(POST("/api/dan"), danService::write).filter(handleFilterFunction::authCheckBeforeFilterFunction);
    }
}

@Configuration
class WebConfig implements WebFluxConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}