package io.taesu.apigw.client

import org.springframework.cache.annotation.Cacheable
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
    @Cacheable(value = ["clientMatcher"])
    fun isMatchedClient(base64Credentials: String): Mono<Boolean> {
        val credDecoded = Base64.getDecoder().decode(base64Credentials)
        val credentials = String(credDecoded, StandardCharsets.UTF_8)
        val (clientId, clientSecret) = credentials.split(":", ignoreCase = false, limit = 2).toTypedArray()

        return this.clientRepository.findById(clientId)
            .map { matches(clientSecret, it) }
            .switchIfEmpty(Mono.just(false)).cache()
    }

    private fun matches(clientSecret: String, it: Client): Boolean {
        return passwordEncoder.matches(clientSecret, it.secret)
    }
}