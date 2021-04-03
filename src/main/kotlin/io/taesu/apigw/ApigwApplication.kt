package io.taesu.apigw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import java.util.*
import javax.annotation.PostConstruct

@EnableCaching
@ConfigurationPropertiesScan
@SpringBootApplication
class ApigwApplication {
    @PostConstruct
    fun onConstruct() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }
}

fun main(args: Array<String>) {
    runApplication<ApigwApplication>(*args)
}
