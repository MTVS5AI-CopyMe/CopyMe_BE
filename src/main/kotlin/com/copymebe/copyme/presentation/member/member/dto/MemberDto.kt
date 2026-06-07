package com.copymebe.copyme.presentation.member.member.dto

import com.copymebe.copyme.core.domain.member.member.models.Member
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

data class MemberDto(
    @Schema(
        description = "Member ID",
        required = true
    )
    val id: UUID,

    @Schema(
        description = "Email",
        required = true
    )
    val email: String,

    @Schema(
        description = "Profile Image URL",
        required = true
    )
    val profileImageUrl: String,

    @Schema(
        description = "Nickname",
        required = true
    )
    val nickname: String,

    @Schema(
        description = "생성일시",
        required = true
    )
    val createdAt: LocalDateTime,

    @Schema(
        description = "수정일시",
        required = true
    )
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(e: Member): MemberDto {
            return MemberDto(
                id = e.id,
                email = e.email,
                profileImageUrl = e.profile.profileImageUrl,
                nickname = e.profile.nickname,
                createdAt = e.createdAt,
                updatedAt = e.updatedAt,
            )
        }
    }
}
