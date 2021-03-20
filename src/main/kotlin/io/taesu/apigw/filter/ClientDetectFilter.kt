package io.taesu.apigw.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.taesu.apigw.client.ClientRetrieveService
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * Created by itaesu on 2021/03/19.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Component
class ClientDetectFilter(val clientRetrieveService: ClientRetrieveService,
                         val objectMapper: ObjectMapper) : GlobalFilter {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val authorizationHeaders: List<String>? = exchange.request.headers["Authorization"]
        if (authorizationHeaders.isNullOrEmpty()) {
            return getUnauthorizedMono(exchange)
        }

        val base64Credentials = authorizationHeaders[0].substringAfter("Basic").trim()
        return clientRetrieveService.isMatchedClient(base64Credentials)
            .flatMap {
                when (it) {
                    true -> chain.filter(exchange)
                    false -> getUnauthorizedMono(exchange, base64Credentials)
                }
            }
    }

    private fun getUnauthorizedMono(exchange: ServerWebExchange, base64Credentials: String = ""): Mono<Void> {

        return with(exchange.response) {
            statusCode = HttpStatus.UNAUTHORIZED
            headers.contentType = MediaType.APPLICATION_JSON
            writeWith(writeResponseBody(exchange, base64Credentials))
        }
    }

    private fun writeResponseBody(exchange: ServerWebExchange,
                                  base64Credentials: String) =
        Mono.fromSupplier {
            with(exchange.response.bufferFactory()) {
                try {
                    wrap(objectMapper.writeValueAsBytes(
                        mapOf(
                            "errorCode" to "INVALID_CLIENT",
                            "errorMessage" to "[${base64Credentials}] is invalid client."
                        )))
                } catch (e: Exception) {
                    wrap(byteArrayOf(0))
                }
            }
        }
}