package com.copymebe.copyme.core.global.exception

import org.springframework.http.HttpStatus

class CustomBadRequestException(
    code: String = "BAD_REQUEST",
    status: HttpStatus = HttpStatus.BAD_REQUEST,
    override val message: String = "Bad Request",
) : BaseException(
    code = code,
    status = status,
    message = message,
)
