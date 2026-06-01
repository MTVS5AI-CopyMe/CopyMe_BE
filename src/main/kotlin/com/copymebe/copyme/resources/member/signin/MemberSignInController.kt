package com.copymebe.copyme.resources.member.signin

import com.copymebe.copyme.core.domain.auth.NotValidMemberCredentialException
import com.copymebe.copyme.core.domain.member.AlreadyExistsMemberException
import com.copymebe.copyme.core.domain.member.MemberRepo
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.ApiExceptions
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class MemberSignInRequest(
    @field:Email
    @Schema(
        description = "이메일",
        example = "user@example.com"
    )
    val email: String,

    @field:NotEmpty
    @Schema(
        description = "비밀번호",
        example = "asdasd123!!"
    )
    val password: String,

    @field:NotEmpty
    @Schema(
        description = "접속 디바이스 ID",
        example = "019e8222-f598-7910-9c48-9f138def99fc"
    )
    val deviceUid: String,

    @field:NotEmpty
    @Schema(
        description = "FCM 토큰 (푸시알림 토큰)",
        example = "019e8222-f598-7910-9c48-9f138def99fc"
    )
    val fcmToken: String,
)

@Tag(name = "Member")
@RestController
class MemberSignInController(
    private val memberRepo: MemberRepo,
    private val passwordEncoder: PasswordEncoder,
) {
    @Operation(summary = "멤버 로그인")
    @ApiExceptions(
        AlreadyExistsMemberException::class,
        NotValidMemberCredentialException::class,
    )
    @PostMapping("/members/signin")
    fun signup(
        @RequestBody @Valid req: MemberSignInRequest,
    ): CustomResponseEntity<UUID> {
        val member = memberRepo.findByEmail(req.email)
            ?: throw AlreadyExistsMemberException()

        passwordEncoder
            .matches(req.password, member.password)
            .let { isMatched ->
                // 비밀번호 미일치시 Throw
                if (isMatched.not()) {
                    throw NotValidMemberCredentialException()
                }
            }

        // TODO: DeviceInfo 추가하기

        return CustomResponseEntity(data = member.id)
    }
}