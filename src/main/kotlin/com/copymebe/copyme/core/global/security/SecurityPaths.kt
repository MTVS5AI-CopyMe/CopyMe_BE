package com.copymebe.copyme.core.global.security

object SecurityPaths {
    val PUBLIC_PATHS = arrayOf(
        // 헬스체크 경로
        "/",
        // 디테일한 에러 표시 (개발 단계)
        "/error",
        // H2 콘솔 허용 (개발 단계)
        "/h2-console/**",
        "/signup",
        "/signin",
    )
}