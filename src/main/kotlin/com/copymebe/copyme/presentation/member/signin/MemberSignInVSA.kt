package com.copymebe.copyme.presentation.member.signin

import com.copymebe.copyme.core.domain.member.auth.InvalidMemberCredentialException
import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.domain.member.member.NotFoundMemberException
import com.copymebe.copyme.core.domain.member.member.models.Member
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.CustomApiExceptions
import com.copymebe.copyme.core.global.security.SecurityJwtTokenProvider
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class MemberSignInRequest(
    @Schema(
        description = "이메일",
        example = "user@example.com"
    )
    @field:Email
    val email: String,

    @Schema(
        description = "비밀번호",
        example = "asdasd123!!"
    )
    @field:NotEmpty
    @field:Pattern(
        regexp = Member.PASSWORD_REGEX,
        message = "영문 대소문자, 숫자, 특수문자(@$!%*?&) 각 1개 이상 포함하는 8~20자"
    )
    val password: String,

    @Schema(
        description = "접속 디바이스 ID",
        example = "019e8222-f598-7910-9c48-9f138def99fc"
    )
    @field:NotEmpty
    val deviceUid: String,

    @Schema(
        description = "FCM 토큰 (푸시알림 토큰)",
        example = "019e8222-f598-7910-9c48-9f138def99fc"
    )
    @field:NotEmpty
    val fcmToken: String,
)

data class MemberSignInDto(
    @Schema(
        description = "Member ID",
        required = true
    )
    val memberId: UUID,

    @Schema(
        description = "Access Token",
        required = true
    )
    val accessToken: String,

    @Schema(
        description = "Refresh Token",
        required = true
    )
    val refreshToken: String,
)

class MemberSignInResponse(
    @Schema(
        description = "데이터",
        required = true
    )
    override val data: MemberSignInDto
) : CustomResponseEntity<MemberSignInDto>()

@Tag(name = "Member SignIn")
@RestController
class MemberSignInVSA(
    private val memberRepo: MemberRepo,
    private val passwordEncoder: PasswordEncoder,
    private val securityJwtTokenProvider: SecurityJwtTokenProvider,
) {
    @Operation(summary = "멤버 로그인")
    @CustomApiExceptions(
        NotFoundMemberException::class,
        InvalidMemberCredentialException::class,
    )
    @PostMapping("/api/v1/members/signin")
    fun signIn(
        @RequestBody @Valid req: MemberSignInRequest,
    ): MemberSignInResponse {
        val member = memberRepo.findByEmailAndDeletedAtNull(req.email)
            ?: throw NotFoundMemberException()

        // 비밀번호 일치 확인
        passwordEncoder
            .matches(req.password, member.password)
            .let { isMatched ->
                // 비밀번호 미일치시 Throw
                if (isMatched.not()) {
                    throw InvalidMemberCredentialException()
                }
            }

        // 토큰 그룹 발급
        val (accessToken, refreshToken) = generateTokenGroup(
            memberId = member.id.toString(),
            memberEmail = member.email
        )

        // 디바이스 정보 덮어쓰기
        member.upsertDevice(
            deviceUid = req.deviceUid,
            fcmToken = req.fcmToken,
            refreshToken = refreshToken
        )

        // 저장
        memberRepo.save(member)

        return MemberSignInResponse(
            data = MemberSignInDto(
                memberId = member.id,
                accessToken = accessToken,
                refreshToken = refreshToken
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