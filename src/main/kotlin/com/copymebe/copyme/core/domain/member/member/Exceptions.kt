package com.copymebe.copyme.core.domain.member.member

import com.copymebe.copyme.core.domain.base.ExceptionMetadata
import com.copymebe.copyme.core.global.exception.BaseException
import org.springframework.http.HttpStatus

class NotFoundMemberException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "NOT_FOUND_MEMBER"
        override val MESSAGE = "멤버 없음"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.BAD_REQUEST
    }
}

class AlreadyExistsMemberException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "ALREADY_EXISTS_MEMBER"
        override val MESSAGE = "이미 가입된 멤버"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.BAD_REQUEST
    }
}

class AlreadyExistsMemberNicknameException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "ALREADY_EXISTS_MEMBER_NICKNAME"
        override val MESSAGE = "이미 사용중인 닉네임"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.BAD_REQUEST
    }
}