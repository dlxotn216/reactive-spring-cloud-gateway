package io.taesu.apigw.app.config

import io.taesu.apigw.filter.LocaleResolverFilterFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
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
class RouteLocatorConfig(private val properties: RouteLocatorProperties,
                         private val localeResolverFilterFactory: LocaleResolverFilterFactory) {
    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator = with(properties) {
        builder.routes()
            .route("sitn-router") { route ->
                route.path(properties.sitn.path)
                    .filters {
                        it.stripPrefix(1)
                        it.filter(localeResolverFilterFactory.apply { it })
                    }
                    .uri(sitn.uri)
            }
            .route("uitn-route") { route ->
                route.path(uitn.path)
                    .filters {
                        it.stripPrefix(1)
                        it.filter(localeResolverFilterFactory.apply { it })
                    }
                    .uri(uitn.uri)
            }
            .route("notification-service-route") { route ->
                route.path(notification.path)
                    .filters {
                        it.stripPrefix(1)
                        it.filter(localeResolverFilterFactory.apply { it })
                    }
                    .uri(notification.uri)
            }
            .build()
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "app.routes")
class RouteLocatorProperties(
    val sitn: Route,
    val uitn: Route,
    val notification: Route,
)

class Route(val path: String, val uri: String)