package com.copymebe.copyme.presentation.member.member

import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.domain.member.member.NotFoundMemberException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.CustomApiExceptions
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.security.getUserId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Member")
@RestController
class MemberRemoveVSA(
    private val memberRepo: MemberRepo,
) {
    @Operation(summary = "회원 탈퇴")
    @CustomApiExceptions(
        NotFoundMemberException::class
    )
    @DeleteMapping("/api/v1/members/me")
    fun remove(
        authentication: Authentication
    ): CustomResponseEntity<UUID> {
        val userId = authentication.getUserId()

        // 회원 조회
        val member = memberRepo.findByIdOrNull(userId)
            ?: throw NotFoundMemberException()

        // 삭제 처리
        member.softDelete()

        // 저장
        memberRepo.save(member)

        return CustomResponseEntity(data = member.id)
    }
}