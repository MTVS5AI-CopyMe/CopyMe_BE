package com.copymebe.copyme.core.domain.auth

import com.copymebe.copyme.core.global.exception.BaseException
import org.springframework.http.HttpStatus

class MaxSignupAuthRequestExceeded(
    code: String = "MAX_SIGNUP_AUTH_REQUEST_EXCEEDED",
    status: HttpStatus = HttpStatus.BAD_REQUEST,
    override val message: String = "Maximum authentication request exceeded",
) : BaseException(
    code = code,
    status = status,
    message = message,
)