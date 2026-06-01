package com.copymebe.copyme.resources.member.signup

import com.copymebe.copyme.core.domain.member.auth.MemberSignupAuthCodeInvalidException
import com.copymebe.copyme.core.domain.member.auth.MemberSignupAuthenticationManagerRepo
import com.copymebe.copyme.core.domain.member.auth.models.MemberSignupAuthenticationManager
import com.copymebe.copyme.core.domain.member.auth.services.MemberSignupAuthTokenProvider
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.ApiExceptions
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
    @field:Email
    @Schema(
        description = "이메일",
        example = "user@example.com"
    )
    val email: String,

    @field:NotEmpty
    @Schema(
        description = "이메일 인증토큰",
        example = "asd123"
    )
    val authCode: String,
)

@Tag(name = "Member Signup")
@RestController
class MemberSignupAuthCodeValidateController(
    private val memberSignupAuthenticationManagerRepo: MemberSignupAuthenticationManagerRepo,
    private val memberSignupAuthTokenProvider: MemberSignupAuthTokenProvider,
) {
    @Operation(summary = "멤버 회원가입 인증코드 검증")
    @ApiExceptions(
        MemberSignupAuthCodeInvalidException::class,
    )
    @PostMapping("/members/signup/authcode-validate")
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
        memberSignupAuthenticationManager.authenticateOrThrow(req.authCode)

        // 저장
        memberSignupAuthenticationManagerRepo.save(memberSignupAuthenticationManager)

        // 이메일 인증토큰
        val emailAuthToken = memberSignupAuthTokenProvider.createToken(email)

        return CustomResponseEntity(data = emailAuthToken)
    }
}