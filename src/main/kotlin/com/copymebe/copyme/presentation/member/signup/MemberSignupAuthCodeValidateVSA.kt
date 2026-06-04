package com.copymebe.copyme.presentation.member.signup

import com.copymebe.copyme.core.domain.member.auth.MemberSignupAuthCodeInvalidException
import com.copymebe.copyme.core.domain.member.auth.MemberSignupAuthenticationManagerRepo
import com.copymebe.copyme.core.domain.member.auth.models.MemberSignupAuthenticationManager
import com.copymebe.copyme.core.domain.member.auth.services.MemberSignupAuthTokenProvider
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.CustomApiExceptions
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class MemberSignupAuthCodeValidateRequest(
    @Schema(
        description = "이메일",
        example = "user@example.com"
    )
    @field:Email
    val email: String,

    @Schema(
        description = "이메일 인증토큰",
        example = "asd123"
    )
    @field:NotEmpty
    val authCode: String,
)

@Tag(name = "Member Signup")
@RestController
class MemberSignupAuthCodeValidateVSA(
    private val memberSignupAuthenticationManagerRepo: MemberSignupAuthenticationManagerRepo,
    private val memberSignupAuthTokenProvider: MemberSignupAuthTokenProvider,
) {
    @Operation(summary = "멤버 회원가입 인증코드 검증", description = "이메일 발송 구현 전까지 인증코드 asd123 고정")
    @CustomApiExceptions(
        MemberSignupAuthCodeInvalidException::class,
    )
    @PostMapping("/api/v1/members/signup/authcode-validate")
    fun validateSignupAuthCode(
        @RequestBody @Valid req: MemberSignupAuthCodeValidateRequest
    ): CustomResponseEntity<String> {
        val email = req.email

        // 회원가입 인증 매니저 불러오기
        val memberSignupAuthenticationManager =
            memberSignupAuthenticationManagerRepo
                .findByEmail(email)
                ?: MemberSignupAuthenticationManager.create(email)

        // 인증 매니저에 인증 요청
        // memberSignupAuthenticationManager.authenticateOrThrow(req.authCode)
        //
        // // 저장
        // memberSignupAuthenticationManagerRepo.save(memberSignupAuthenticationManager)

        // 외부 이메일 발송 서비스 구현까지 임시 처리
        run {
            if (req.authCode != "asd123") {
                throw MemberSignupAuthCodeInvalidException()
            }
            memberSignupAuthenticationManager.requests.clear()
        }

        // 이메일 인증토큰
        val emailAuthToken = memberSignupAuthTokenProvider.createToken(email)

        return CustomResponseEntity(data = emailAuthToken)
    }
}