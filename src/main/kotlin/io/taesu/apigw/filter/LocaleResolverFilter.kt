package io.taesu.apigw.filter

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.SetRequestHeaderGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.i18n.LocaleContextResolver
import reactor.core.publisher.Mono
import java.util.*

/**
 * Created by itaesu on 2021/04/02.
 *
 * {@code LocaleContextResolver}를 통해서 Resolve 된 Locale로 AcceptLanguage 헤더를 구성하는 필터
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 * @see io.taesu.apigw.app.config.ChainedAcceptHeaderLocaleResolver
 */
@Component
class LocaleResolverFilterFactory(private val localeContextResolver: LocaleContextResolver)
    : SetRequestHeaderGatewayFilterFactory() {

    override fun apply(config: NameValueConfig): GatewayFilter {
        return LocaleResolverFilter(localeContextResolver)
    }

    override fun newConfig(): NameValueConfig {
        return NameValueConfig()
    }
}

class LocaleResolverFilter(
    private val localeContextResolver: LocaleContextResolver) : GatewayFilter {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val resolveLocaleContext = localeContextResolver.resolveLocaleContext(exchange)
        val locale = resolveLocaleContext.locale ?: Locale.ENGLISH
        exchange.request.mutate().headers {
            it.acceptLanguage = listOf(Locale.LanguageRange(locale.toLanguageTag()))
        }
        return chain.filter(exchange)
    }
}