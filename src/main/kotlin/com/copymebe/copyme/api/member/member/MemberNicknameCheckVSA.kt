package com.copymebe.copyme.api.member.member

import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class MemberNicknameCheckRequest(
    @field:NotEmpty
    @Schema(
        description = "닉네임",
        example = "asd"
    )
    val nickname: String
)

@Tag(name = "Member")
@RestController
class MemberNicknameCheckVSA(
    private val memberRepo: MemberRepo,
) {
    @Operation(summary = "멤버 닉네임 중복검사")
    @GetMapping("/members/nickname-check")
    fun checkNickname(@Valid req: MemberNicknameCheckRequest): CustomResponseEntity<Boolean> {
        val isValidNickname = memberRepo.findByProfileNickname(req.nickname) == null

        return CustomResponseEntity(data = isValidNickname)
    }
}