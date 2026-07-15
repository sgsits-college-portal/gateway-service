package com.collegeportal.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(RouteValidator validator, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.validator = validator;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                
                // 1. Check if Authorization header is present
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    // Extract token if you needed it, but we can just forward the whole header
                }

                // 2. Call auth-service to validate token
                return webClientBuilder.build()
                        .get()
                        .uri("http://AUTH-SERVICE/api/auth/validate")
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .retrieve()
                        .toBodilessEntity()
                        .flatMap(response -> {
                            // If successful, continue the filter chain
                            return chain.filter(exchange);
                        })
                        .onErrorResume(error -> {
                            // If auth-service returns 401 or any error, reject request
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        });
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
