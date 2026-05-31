package com.copymebe.copyme.core.global.security

import com.copymebe.copyme.core.global.exception.BaseException
import org.springframework.http.HttpStatus

class ExpiredAccessTokenException(
    code: String = "EXPIRED_ACCESS_TOKEN",
    status: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val message: String = "Expired Access Token",
) : BaseException(
    code = code,
    status = status,
    message = message,
)