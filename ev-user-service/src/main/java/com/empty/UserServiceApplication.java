package com.empty;

import com.empty.auth.basic.BasicAuthenticationSuccessHandler;
import com.empty.auth.bearer.BearerTokenReactiveAuthenticationManager;
import com.empty.auth.bearer.ServerHttpBearerAuthenticationConverter;
import com.empty.repository.UserRepository;
import com.empty.service.AuthService;
import com.empty.service.UserService;
import com.empty.web.HandleFilterFunction;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@SpringBootApplication
@EnableDiscoveryClient
@EnableReactiveMongoRepositories
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

@Configuration
class RouterFunctionConfig {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    HandleFilterFunction handleFilterFunction;

    @Bean
    RouterFunction<ServerResponse> userRouterFunction() {
        return route(GET("/user/{id}"), userService::getUser)
                .andRoute(POST("/user"), userService::register);
    }

    @Bean
    RouterFunction<ServerResponse> userPatchUserFunction() {
        return route(PATCH("/user"), userService::updateProfile)
                .filter(handleFilterFunction::userAfterFilterHandle);
    }

    @Bean
    RouterFunction<ServerResponse> authRouterFunction() {
        return route(POST("/auth/login"), authService::getMapAuthMono)
                .andRoute(GET("/auth/active/{sessionId}"), authService::activeAccount)
                .filter(handleFilterFunction::userAfterFilterHandle);
    }

    @Bean
    RouterFunction<ServerResponse> authMiddlewareRouterFunction() {
        return route(GET("/auth/user"), authService::getMapAuthMono);
    }
}

@Configuration
class WebConfig implements WebFluxConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Autowired
    UserRepository userRepository;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http.authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/auth/login").authenticated()
                .and().addFilterAt(basicAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/auth/user").authenticated()
                .and().addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .pathMatchers(HttpMethod.PATCH, "/user").authenticated()
                .and().addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .anyExchange().permitAll();
        return http.csrf().disable().build();
    }

    private AuthenticationWebFilter basicAuthenticationFilter() {
        UserDetailsRepositoryReactiveAuthenticationManager authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
        ServerAuthenticationSuccessHandler successHandler = new BasicAuthenticationSuccessHandler();

        AuthenticationWebFilter basicAuthenticationFilter = new AuthenticationWebFilter(authManager);
        basicAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        basicAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/auth/login"));
        return basicAuthenticationFilter;
    }

    private AuthenticationWebFilter bearerAuthenticationFilter() {
        ServerAuthenticationConverter bearerConverter = new ServerHttpBearerAuthenticationConverter();
        ReactiveAuthenticationManager authManager = new BearerTokenReactiveAuthenticationManager();
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);

        bearerAuthenticationFilter.setServerAuthenticationConverter(bearerConverter);
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/auth/user", "/user"));
        return bearerAuthenticationFilter;
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return username -> userRepository.findUserByEmail(username).switchIfEmpty(Mono.empty())
                .map(myUser -> User.withUsername(myUser.getId())
                        .password(myUser.getPwd())
                        .authorities(myUser.getSystem().getRoles().toArray(new String[0]))
                        .build()
                );
    }
}

@Configuration
class KafkaConfig {
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

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        //props.put(ConsumerConfig.GROUP_ID_CONFIG, "ev-consumer");
        //props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Map<String, Object>> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(),
                new StringDeserializer(),
                new JsonDeserializer<>(Map.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Map<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Map<String, Object>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}







