package com.empty;

import com.empty.auth.basic.BasicAuthenticationSuccessHandler;
import com.empty.auth.bearer.BearerTokenReactiveAuthenticationManager;
import com.empty.auth.bearer.ServerHttpBearerAuthenticationConverter;
import com.empty.repository.UserRepository;
import com.empty.service.AuthService;
import com.empty.service.SessionService;
import com.empty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.HttpMethod;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@SpringBootApplication
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
    SessionService sessionService;

    @Bean
    RouterFunction<ServerResponse> userRouterFunction() {
        return route(GET("/api/user/{id}"), userService::getUser)
                .andRoute(POST("/api/user"), userService::register)
                .andRoute(PATCH("/api/user"), userService::updateProfile);
    }

    @Bean
    RouterFunction<ServerResponse> authRouterFunction() {
        return route(POST("/auth/login"), authService::getMapAuthMono)
                .andRoute(GET("/auth/user"), authService::getMapAuthMono)
                .andRoute(GET("/auth/active/{sessionId}"), authService::activeAccount);
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
                .pathMatchers(HttpMethod.PATCH, "/api/user").authenticated()
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
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/auth/user", "/api/user"));
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








