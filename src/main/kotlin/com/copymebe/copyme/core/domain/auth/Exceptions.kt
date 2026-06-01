package com.copymebe.copyme.core.domain.auth

import com.copymebe.copyme.core.domain.base.ExceptionMetadata
import com.copymebe.copyme.core.global.exception.BaseException
import org.springframework.http.HttpStatus

class MaxSignupAuthRequestExceededException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "MAX_SIGNUP_AUTH_REQUEST_EXCEEDED"
        override val MESSAGE = "인증 요청 횟수 초과 (10분 대기)"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.BAD_REQUEST
    }
}

class SignupAuthCodeExpiredException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "SIGNUP_AUTH_CODE_EXPIRED"
        override val MESSAGE = "인증코드 만료"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.UNAUTHORIZED
    }
}
