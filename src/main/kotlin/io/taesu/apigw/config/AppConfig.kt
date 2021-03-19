package io.taesu.apigw.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Created by itaesu on 2021/03/19.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Configuration
class AppConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder = DelegatingPasswordEncoder(
        "bcrypt",
        mapOf(
            "bcrypt" to BCryptPasswordEncoder()
        )
    )
}