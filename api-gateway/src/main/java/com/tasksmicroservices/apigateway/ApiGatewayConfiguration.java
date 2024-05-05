package com.tasksmicroservices.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Autowired
    private UserAuthFilter userAuthFilter;

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/api/v1/tasks/**")
                        .filters(f -> f.filter(userAuthFilter))
                        .uri("lb://task-service"))
                .route(p -> p.path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                .build();
    }

}
