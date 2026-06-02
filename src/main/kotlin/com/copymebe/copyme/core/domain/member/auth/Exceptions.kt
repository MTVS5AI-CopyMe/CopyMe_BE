package com.copymebe.copyme.core.domain.member.auth

import com.copymebe.copyme.core.domain.base.ExceptionMetadata
import com.copymebe.copyme.core.global.exception.BaseException
import org.springframework.http.HttpStatus

class MaxSignupAuthRequestExceededException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "EXCEEDED_MAX_SIGNUP_AUTH_REQUEST"
        override val MESSAGE = "인증 요청 횟수 초과 (10분 대기)"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.BAD_REQUEST
    }
}

class MemberSignupAuthCodeInvalidException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "INVALID_MEMBER_SIGNUP_AUTH_CODE"
        override val MESSAGE = "인증코드 유효하지 않음"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.UNAUTHORIZED
    }
}

class MemberSignupEmailTokenInvalidException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "INVALID_MEMBER_SIGNUP_EMAIL_TOKEN"
        override val MESSAGE = "이메일 토큰 유효하지 않음"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.UNAUTHORIZED
    }
}

class InvalidMemberCredentialException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "INVALID_MEMBER_CREDENTIAL"
        override val MESSAGE = "인증 실패"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.UNAUTHORIZED
    }
}

class MemberRefreshTokenExpiredException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "EXPIRED_REFRESH_TOKEN"
        override val MESSAGE = "리프레쉬 토큰 만료"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.UNAUTHORIZED
    }
}