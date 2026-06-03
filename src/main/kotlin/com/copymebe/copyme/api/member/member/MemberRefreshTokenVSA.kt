package com.copymebe.copyme.api.member.member

import com.copymebe.copyme.core.domain.member.auth.MemberRefreshTokenExpiredException
import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.CustomApiExceptions
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.security.CustomForbiddenException
import com.copymebe.copyme.core.global.security.SecurityJwtTokenProvider
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

data class MemberRefreshTokenResponse(
    @Schema(description = "Access Token")
    val accessToken: String,

    @Schema(description = "Refresh Token")
    val refreshToken: String,
)

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Member")
@RestController
class MemberRefreshTokenVSA(
    private val memberRepo: MemberRepo,
    private val securityJwtTokenProvider: SecurityJwtTokenProvider
) {
    @Operation(summary = "멤버 액세스 토큰 재발급")
    @CustomApiExceptions(
        MemberRefreshTokenExpiredException::class
    )
    @PostMapping("/api/v1/members/refresh-token")
    fun refreshToken(
        authentication: Authentication
    ): CustomResponseEntity<MemberRefreshTokenResponse> {
        val oldRefreshToken = authentication.credentials as String

        // Refresh Token으로 Member 찾기
        val member = memberRepo.findByDevicesRefreshToken(oldRefreshToken)
            ?: throw CustomForbiddenException()

        // 디바이스 조회
        val targetDevice = member.devices.find { it.refreshToken == oldRefreshToken }
            ?: throw CustomForbiddenException()

        // 토큰 그룹 발급
        val (newAccessToken, newRefreshToken) = generateTokenGroup(
            memberId = member.id.toString(),
            memberEmail = member.email
        )

        // 디바이스 정보 덮어쓰기
        member.upsertDevice(
            deviceUid = targetDevice.deviceUid,
            fcmToken = targetDevice.fcmToken,
            refreshToken = newRefreshToken
        )

        // 저장
        memberRepo.save(member)

        return CustomResponseEntity(
            data = MemberRefreshTokenResponse(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
        )
    }

    /**
     * 토큰 그룹 발급
     */
    private fun generateTokenGroup(
        memberId: String,
        memberEmail: String
    ): Pair<String, String> {
        return UsernamePasswordAuthenticationToken(
            memberEmail,
            ""
        ).let { authentication ->
            val accessToken = securityJwtTokenProvider.createAccessToken(
                authentication = authentication,
                userId = memberId,
            )

            val refreshToken = securityJwtTokenProvider.createRefreshToken(
                authentication = authentication,
                userId = memberId,
            )

            Pair(accessToken, refreshToken)
        }
    }
}