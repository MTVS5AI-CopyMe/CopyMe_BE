package com.copymebe.copyme.core.global.exception.handlers

import com.copymebe.copyme.core.global.http.CustomResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


private data class FieldError(
    val field: String,
    val value: Any?,
    val reason: String
)

@RestControllerAdvice
class GlobalValidationExceptionHandler {
    /**
     * Validation Exception 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<Any> {
        val fieldErrors = e.extractFieldErrors()
        val resultError = fieldErrors.ifEmpty { e.extractAllErrors() }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                CustomResponseEntity(
                    code = "VALIDATION_ERROR",
                    message = "Invalid request",
                    data = resultError
                )
            )
    }
}

/**
 * fieldErrors to FieldErrors
 */
private fun MethodArgumentNotValidException.extractFieldErrors(): List<FieldError> =
    this.bindingResult.fieldErrors.map { fieldError ->
        FieldError(
            field = fieldError.field,
            value = fieldError.rejectedValue,
            reason = fieldError.defaultMessage ?: ""
        )
    }

/**
 * allErrors to FieldErrors
 */
private fun MethodArgumentNotValidException.extractAllErrors(): List<FieldError> =
    this.allErrors.mapNotNull { error ->
        // 에러 메시지 예시
        // "Parameter specified as non-null is null: method ...Request.<init>, parameter nickname"
        error.defaultMessage
            ?.split(":")
            ?.let { (reason, messageLast) ->
                // reason = "Parameter specified as non-null is null"
                // messageLast = "method ...Request.<init>, parameter nickname"

                // "nickname"
                val field = messageLast.split(" ").last()

                FieldError(
                    field = field,
                    value = null,
                    reason = reason
                )
            }
    }
