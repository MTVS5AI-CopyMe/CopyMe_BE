package com.copymebe.copyme.core.global.http

import com.copymebe.copyme.core.global.exception.BaseException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BaseException::class)
    fun handleHttpClientErrorException(e: BaseException): ResponseEntity<CustomResponseEntity<Nothing>> {
        return ResponseEntity
            .status(e.status)
            .body(
                CustomResponseEntity(
                    code = e.code,
                    message = e.message,
                    data = null
                )
            )
    }
}