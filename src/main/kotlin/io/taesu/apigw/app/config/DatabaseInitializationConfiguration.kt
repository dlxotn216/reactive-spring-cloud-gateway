package io.taesu.apigw.app.config

import io.taesu.apigw.client.Client
import io.taesu.apigw.client.ClientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.util.StreamUtils
import java.nio.charset.Charset


/**
 * Created by itaesu on 2021/03/02.
 *
 * @author Lee Tae Su
 * @version TBD
 * @since TBD
 */
@Configuration(proxyBeanMethods = false)
class DatabaseInitializationConfiguration(
    private val clientRepository: ClientRepository
) {
    /**
     * https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-initialize-a-database-using-r2dbc
     * R2dbc를 사용하는 경우 auto-configuration이 off 됨
     */
    @Autowired
    fun initializeDatabase(r2dbcEntityTemplate: R2dbcEntityTemplate) {
        val schema = StreamUtils.copyToString(
            ClassPathResource("schema.sql").inputStream,
            Charset.defaultCharset()
        )
        r2dbcEntityTemplate.databaseClient.sql(schema).fetch().rowsUpdated().block()

        clientRepository.saveAll(
            listOf(
                Client(id = "SITN",
                   secret = "{bcrypt}\$2a\$10\$hjlo0qUWHVedUnOg5kJlH.2gOQNKdbeBIsIIQCqIKNCsFVgUkCUKK",
                   connectURL = "https://www.cubecdms.com"),
                Client(id = "APP_LMS_DEV",
                   secret = "{bcrypt}\$2a\$10\$hjlo0qUWHVedUnOg5kJlH.2gOQNKdbeBIsIIQCqIKNCsFVgUkCUKK",
                   connectURL = "https://www.cubecdms.com"),
                Client(id = "APP_SAFETY_R3",
                   secret = "{bcrypt}\$2a\$10\$hjlo0qUWHVedUnOg5kJlH.2gOQNKdbeBIsIIQCqIKNCsFVgUkCUKK",
                   connectURL = "https://www.cubecdms.com"),
                Client(id = "APP_TMF_DEV",
                   secret = "{bcrypt}\$2a\$10\$hjlo0qUWHVedUnOg5kJlH.2gOQNKdbeBIsIIQCqIKNCsFVgUkCUKK",
                   connectURL = "https://www.cubecdms.com"),
            )
        ).subscribe()
    }
}