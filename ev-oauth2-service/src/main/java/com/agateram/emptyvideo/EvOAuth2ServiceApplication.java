package com.agateram.emptyvideo;

import com.agateram.emptyvideo.auth.bearer.BearerTokenReactiveAuthenticationManager;
import com.agateram.emptyvideo.auth.bearer.ServerHttpBearerAuthenticationConverter;
import com.agateram.emptyvideo.service.OAuthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableDiscoveryClient
@SpringBootApplication
public class EvOAuth2ServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EvOAuth2ServiceApplication.class, args);
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}

@Configuration
class RouterFunctionConfig {
	@Autowired
	OAuthClientService oAuthClientService;
	@Bean
	RouterFunction<ServerResponse> oauth2RouterFunctions() {
		return route(POST("/oauth/code"), oAuthClientService::generateOauthCode)
				.andRoute(GET("/oauth/userInfo"), oAuthClientService::getUserInfo)
				.andRoute(POST("/oauth/client"), oAuthClientService::createNewClient);
	}
}

@Configuration
class SecurityConfig {

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
		http.authorizeExchange()
				.pathMatchers(HttpMethod.GET, "/oauth/userInfo").authenticated()
				.and().addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
				.authorizeExchange()
				.anyExchange().permitAll();
		return http.csrf().disable().build();
	}

	private AuthenticationWebFilter bearerAuthenticationFilter() {
		ServerAuthenticationConverter bearerConverter = new ServerHttpBearerAuthenticationConverter();
		ReactiveAuthenticationManager authManager = new BearerTokenReactiveAuthenticationManager();
		AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);

		bearerAuthenticationFilter.setServerAuthenticationConverter(bearerConverter);
		bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/oauth/userInfo"));
		return bearerAuthenticationFilter;
	}
}








