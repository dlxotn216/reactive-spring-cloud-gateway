package io.taesu.apigw.app.config

import org.springframework.context.i18n.LocaleContext
import org.springframework.context.i18n.SimpleLocaleContext
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.i18n.LocaleContextResolver
import java.util.*

/**
 * 아래의 우선순위에 따라 Locale을 Resolver 하는 LocaleContextResolver
 * - Query param
 * - Request Header (Accept-Language)
 */
class ChainedAcceptHeaderLocaleResolver : LocaleContextResolver {
    companion object {
        private val ACCEPTABLE_LANGUAGE = listOf("ko", "en", "ja", "zh")
        private val ACCEPTABLE_LOCALES = listOf(Locale.forLanguageTag("ko"),
                                                Locale.forLanguageTag("en"),
                                                Locale.forLanguageTag("ja"),
                                                Locale.forLanguageTag("zh"))
        private val DEFAULT_LOCALE = Locale.ENGLISH
    }

    override fun resolveLocaleContext(exchange: ServerWebExchange): LocaleContext {
        val query = exchange.request.queryParams.getFirst("lang") ?: ""
        if (query in ACCEPTABLE_LANGUAGE) {
            return SimpleLocaleContext(Locale.forLanguageTag(query))
        }

        val locale = when (val header = Locale.lookup(exchange.request.headers.acceptLanguage, ACCEPTABLE_LOCALES)) {
            in ACCEPTABLE_LOCALES -> header
            else                  -> DEFAULT_LOCALE
        }
        return SimpleLocaleContext(locale)
    }

    override fun setLocaleContext(exchange: ServerWebExchange, localeContext: LocaleContext?) {
        throw UnsupportedOperationException("Not Supported")
    }
}