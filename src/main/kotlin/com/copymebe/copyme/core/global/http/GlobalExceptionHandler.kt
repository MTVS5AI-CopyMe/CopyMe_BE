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

    // TODO: Validator 가드시 에러 포맷 정리하기
    // @ExceptionHandler(MethodArgumentNotValidException::class)
    // fun handle(e: MethodArgumentNotValidException): ResponseEntity<Any> {
    //     println(e.toString())
    //
    //     val errors = e.bindingResult.fieldErrors.map { fieldError ->
    //         mapOf(
    //             "field" to fieldError.field,
    //             "value" to fieldError.rejectedValue,
    //             "reason" to (fieldError.defaultMessage ?: "invalid")
    //         )
    //     }
    //
    //     return ResponseEntity
    //         .status(HttpStatus.BAD_REQUEST)
    //         .body(
    //             mapOf(
    //                 "code" to "VALIDATION_ERROR",
    //                 "message" to "요청 값이 올바르지 않습니다",
    //                 "errors" to errors
    //             )
    //         )
    // }
}