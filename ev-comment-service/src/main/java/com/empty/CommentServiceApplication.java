package com.empty;

import com.empty.service.CommentService;
import com.empty.web.HandleFilterFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableDiscoveryClient
@SpringBootApplication
public class CommentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentServiceApplication.class, args);
    }
}

@Slf4j
@Configuration
class RouterFunctionConfig {

    @Autowired
    CommentService commentService;
    @Autowired
    HandleFilterFunction handleFilterFunction;

    @Bean
    RouterFunction<ServerResponse> commentRouterFunction() {
        return route(GET("/comment/{id}"), commentService::getById)
                .andRoute(GET("/comment/video/{id}"), commentService::getByVideoId);
    }

    @Bean
    RouterFunction<ServerResponse> commentWithAuthRouterFunction() {
        return route(POST("/comment"), commentService::write)
                .andRoute(PATCH("/comment/{id}/like"), commentService::likeCommentById)
                .andRoute(DELETE("/comment/{id}"), commentService::deleteById)
                .filter(handleFilterFunction::authCheckBeforeFilterFunction)
                .filter(handleFilterFunction::commentAfterFilterFunction);
    }
}

@Configuration
class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Map<String, Object>> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Map<String, Object>> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}

