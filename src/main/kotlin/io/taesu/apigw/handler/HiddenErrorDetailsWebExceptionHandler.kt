package io.taesu.apigw.handler

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

/**
 * Created by itaesu on 2021/03/20.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Component
@Order(-2)
class HiddenErrorDetailsWebExceptionHandler(
    errorAttributes: ErrorAttributes,
    resourceProperties: WebProperties.Resources,
    applicationContext: ApplicationContext,
    private val configurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, resourceProperties, applicationContext) {

    init {
        super.setMessageWriters(this.configurer.writers)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), { this.renderErrorResponse(it) })
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults())
        with(errorAttributes) {
            when (this["status"]) {
                404 -> logger.error("Gateway route path not found: {}", this["path"])
                502 -> logger.error("Gateway - Bad gateway: path: [${this["path"]}]")
                503 -> logger.error("Gateway - Service unreachable: path: [${this["path"]}]")
                else -> logger.error("Gateway error occur: $this")
            }
        }
        val map: Map<String, String> =
            errorAttributes.entries
                .filter { it.value != null }
                .map { it.key to it.value.toString() }.toMap()

        val httpStatus = getHttpStatus(errorAttributes)

        return ServerResponse.status(httpStatus)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ErrorResponse(
                    errorCode = "UNHANDLED_ERROR",
                    errorAttributes = map
                ))
    }

    private fun getHttpStatus(errorAttributes: Map<String, Any>): HttpStatus {
        val statusCode = errorAttributes["status"] as Int
        return HttpStatus.valueOf(statusCode)
    }
}

class ErrorResponse(
    val errorCode: String,
    val errorAttributes: Map<String, String>
)