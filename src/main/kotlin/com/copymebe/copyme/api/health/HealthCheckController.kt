package com.copymebe.copyme.api.health

import com.copymebe.copyme.core.global.http.CustomResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @GetMapping("/")
    fun healthCheck(): CustomResponseEntity<String> {
        return CustomResponseEntity(data = "OK")
    }
}