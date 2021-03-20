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
        val message = with(errorAttributes) {
            when (this["status"]) {
                404 -> "Route path not found: [${this["path"]}]".apply { logger.error(this) }
                502 -> "Bad gateway: [${this["path"]}]".apply { logger.error(this) }
                503 -> "Service unreachable: [${this["path"]}]".apply { logger.error(this) }
                else -> "Another error: $this".apply { logger.error(this) }
            }
        }
        val errorAttributesMap: Map<String, String> = errorAttributes.entries
            .filter { it.value != null }
            .map { it.key to it.value.toString() }.toMap()

        return ServerResponse.status(getHttpStatus(errorAttributes))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(ErrorResponse(
                errorCode = "UNHANDLED_ERROR",
                errorAttributes = errorAttributesMap,
                errorMessage = message
            ))
    }

    private fun getHttpStatus(errorAttributes: Map<String, Any>): HttpStatus {
        val statusCode = (errorAttributes["status"] ?: "400") as Int
        return HttpStatus.valueOf(statusCode)
    }
}

class ErrorResponse(
    val errorCode: String,
    val errorAttributes: Map<String, String> = mapOf(),
    val errorMessage: String
)