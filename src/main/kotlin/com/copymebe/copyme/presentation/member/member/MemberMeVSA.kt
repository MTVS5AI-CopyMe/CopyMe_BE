package com.copymebe.copyme.presentation.member.member

import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.domain.member.member.NotFoundMemberException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.security.getUserId
import com.copymebe.copyme.presentation.member.member.dto.MemberDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

class MemberMeResponse(
    @Schema(
        description = "데이터",
        required = true
    )
    override val data: MemberDto
) : CustomResponseEntity<MemberDto>()

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Member")
@RestController
class MemberMeVSA(
    private val memberRepo: MemberRepo,
) {
    @Operation(summary = "내 정보 조회")
    @GetMapping("/api/v1/members/me")
    fun findMe(
        authentication: Authentication
    ): MemberMeResponse {
        val userId = authentication.getUserId()

        // 회원 조회
        val member = memberRepo.findByIdOrNull(userId)
            ?: throw NotFoundMemberException()

        val dto = MemberDto.fromEntity(member)

        return MemberMeResponse(data = dto)
    }
}