package io.taesu.apigw.client

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Created by itaesu on 2021/03/19.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Component
class ClientRetrieveService(
    val clientRepository: ClientRepository,
    val passwordEncoder: PasswordEncoder
) {
    fun isMatchedClient(base64Credentials: String): Mono<Boolean> {
        val credDecoded = Base64.getDecoder().decode(base64Credentials)
        val credentials = String(credDecoded, StandardCharsets.UTF_8)
        val values = credentials.split(":")
        val clientId = values[0]
        val clientSecret = values[1]
        val client = this.clientRepository.findById(clientId)

        return client
            .map {
                passwordEncoder.matches(clientSecret, it.secret)
            }
            .switchIfEmpty(Mono.just(false))
    }
}