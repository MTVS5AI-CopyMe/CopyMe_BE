package com.copymebe.copyme.core.domain.member

import com.copymebe.copyme.core.global.exception.BaseException
import org.springframework.http.HttpStatus

class MemberNotFoundException(
    code: String = "MEMBER_NOT_FOUND",
    status: HttpStatus = HttpStatus.BAD_REQUEST,
    override val message: String = "Member not found",
) : BaseException(
    code = code,
    status = status,
    message = message,
)