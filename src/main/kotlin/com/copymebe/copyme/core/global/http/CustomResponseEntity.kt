package com.copymebe.copyme.core.global.http

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 공통 응답 규격 (봉투 패턴)
 */
open class CustomResponseEntity<T>(
    @Schema(
        description = "응답 코드",
        required = true
    )
    val code: String = "OK",
    @Schema(
        description = "응답 메세지",
        required = true
    )
    val message: String = "OK",
    @Schema(description = "데이터")
    open val data: T? = null
)
