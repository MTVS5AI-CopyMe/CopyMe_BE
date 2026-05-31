package com.copymebe.copyme.core.global.http

/**
 * 공통 응답 규격 (봉투 패턴)
 */
data class CustomResponseEntity<T>(
    val code: String = "OK",
    val message: String = "OK",
    val data: T?
)