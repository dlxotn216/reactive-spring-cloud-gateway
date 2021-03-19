package io.taesu.apigw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApigwApplication

fun main(args: Array<String>) {
    runApplication<ApigwApplication>(*args)
}
