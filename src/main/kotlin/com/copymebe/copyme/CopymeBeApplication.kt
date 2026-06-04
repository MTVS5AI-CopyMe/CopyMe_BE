package com.copymebe.copyme

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class CopymeBeApplication

fun main(args: Array<String>) {
    runApplication<CopymeBeApplication>(*args)
}
