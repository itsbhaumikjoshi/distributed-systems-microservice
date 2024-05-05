package com.tasksmicroservices.apigateway;

import com.tasksmicroservices.apigateway.dto.Introspect;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserAuthFilter implements GatewayFilter {

    private final WebClient webClient;

    public UserAuthFilter() {
        this.webClient = WebClient.create();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final String header = request.getHeaders().getOrEmpty("Authorization").get(0);
        if(header == null || !header.startsWith("Bearer ")) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        return webClient
                .get()
                .uri("http://localhost:3100/api/v1/auth/introspect")
                .headers(headers -> headers.addAll(exchange.getRequest().getHeaders()))
                .retrieve()
                .bodyToMono(Introspect.class)
                .flatMap(response -> {
                    ServerHttpRequest withUserIdHeader = exchange
                            .getRequest()
                            .mutate()
                            .headers(h -> h.add("X-USER-ID", response.getUserId()))
                            .build();
                    return chain.filter(exchange.mutate().request(request).build());
                });
    }

    public interface Config {

    }

}
