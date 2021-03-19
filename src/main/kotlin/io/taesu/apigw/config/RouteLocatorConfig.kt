package io.taesu.apigw.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by itaesu on 2021/03/19.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Configuration
class RouteLocatorConfig {
    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator = builder.routes()
        .route { route ->
            route.path("/sitn/**")
                .filters {
                    it.stripPrefix(1)
                }
                .uri("http://localhost:8090")
        }.build()
}