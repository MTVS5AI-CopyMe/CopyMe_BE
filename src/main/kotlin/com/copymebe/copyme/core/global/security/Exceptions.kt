package com.copymebe.copyme.core.global.security

import com.copymebe.copyme.core.domain.base.ExceptionMetadata
import com.copymebe.copyme.core.global.exception.BaseException
import org.springframework.http.HttpStatus

class ExpiredSecurityTokenException(
    code: String = "EXPIRED_SECURITY_TOKEN",
    status: HttpStatus = HttpStatus.UNAUTHORIZED,
    override val message: String = "Expired Security Token",
) : BaseException(
    code = code,
    status = status,
    message = message,
)

class CustomUnauthorizedException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "UNAUTHORIZED"
        override val MESSAGE = "UNAUTHORIZED"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.UNAUTHORIZED
    }
}

class CustomForbiddenException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "FORBIDDEN"
        override val MESSAGE = "FORBIDDEN"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.FORBIDDEN
    }
}