package com.copymebe.copyme.presentation.member.member

import com.copymebe.copyme.core.domain.member.member.AlreadyExistsMemberNicknameException
import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.global.exception.CustomBadRequestException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.CustomApiExceptions
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.security.getUserId
import com.copymebe.copyme.presentation.member.member.dto.MemberDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class MemberProfileUpdateRequest(
    @Schema(
        description = "프로필 이미지 URL",
        example = "https://picsum.photos/seed/picsum/200/300"
    )
    @field:URL
    val profileImageUrl: String,

    @Schema(
        description = "닉네임",
        example = "asd"
    )
    @field:NotEmpty
    @field:Size(min = 1, max = 12)
    val nickname: String,
)

class MemberProfileUpdateResponse(
    @Schema(
        description = "데이터",
        required = true
    )
    override val data: MemberDto
) : CustomResponseEntity<MemberDto>()

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Member")
@RestController
class MemberProfileUpdateVsa(
    private val memberRepo: MemberRepo,
) {
    @Operation(summary = "멤버 프로필 수정")
    @CustomApiExceptions(
        AlreadyExistsMemberNicknameException::class,
    )
    @PatchMapping("/api/v1/members/me/profile")
    fun update(
        authentication: Authentication,
        @RequestBody @Valid req: MemberProfileUpdateRequest
    ): MemberProfileUpdateResponse {
        val userId = authentication.getUserId()

        // 회원 조회
        val member = memberRepo.findByIdOrNull(userId)
            ?: throw CustomBadRequestException("존재하지 않는 회원 ID")

        // 닉네임 중복 확인
        memberRepo.findByProfileNickname(req.nickname)
            ?.let { duplicatedMember ->
                // 다른 회원이 닉네임을 사용하고 있음
                if (duplicatedMember.id != member.id) {
                    throw AlreadyExistsMemberNicknameException()
                }
            }

        // 프로필 수정
        member.updateProfile(
            profileImageUrl = req.profileImageUrl,
            nickname = req.nickname,
        )

        // 저장
        memberRepo.save(member)

        return MemberProfileUpdateResponse(
            data = MemberDto.fromEntity(member)
        )
    }
}
