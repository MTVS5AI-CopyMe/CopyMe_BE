package com.copymebe.copyme.core.domain.member

import com.copymebe.copyme.core.domain.base.ExceptionMetadata
import com.copymebe.copyme.core.global.exception.BaseException
import org.springframework.http.HttpStatus

class MemberNotFoundException : BaseException(
    code = CODE,
    status = HTTP_STATUS,
    message = MESSAGE,
) {
    companion object : ExceptionMetadata {
        override val CODE = "MEMBER_NOT_FOUND"
        override val MESSAGE = "Member not found"
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
        override val MESSAGE = "Already exists member"
        override val DESCRIPTION = "$CODE: $MESSAGE"
        override val HTTP_STATUS = HttpStatus.BAD_REQUEST
    }
}
