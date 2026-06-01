package com.copymebe.copyme.resources.member.signup

import com.copymebe.copyme.core.domain.auth.SignupAuthCodeExpiredException
import com.copymebe.copyme.core.domain.auth.SignupAuthenticationManagerRepo
import com.copymebe.copyme.core.domain.auth.models.SignupAuthenticationManager
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.ApiExceptions
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class SignupAuthCodeValidateRequest(
    val email: String,
    val authCode: String,
)

@Tag(name = "Member")
@RestController
class SignupAuthCodeValidateController(
    private val signupAuthenticationManagerRepo: SignupAuthenticationManagerRepo,
) {
    @Operation(summary = "멤버 회원가입 인증코드 검증")
    @ApiResponse(responseCode = "200")
    @ApiExceptions(
        SignupAuthCodeExpiredException::class,
    )
    @PostMapping("/members/signup/authcode-validate")
    fun validateSignupAuthCode(
        @RequestBody @Valid req: SignupAuthCodeValidateRequest
    ): CustomResponseEntity<String> {
        val email = req.email

        // 회원가입 인증 매니저 불러오기
        val signupAuthenticationManager =
            signupAuthenticationManagerRepo
                .findByEmail(email)
                ?: SignupAuthenticationManager.create(email)

        // 인증 매니저에 인증 요청
        signupAuthenticationManager.authenticateOrThrow(req.authCode)

        // 저장
        signupAuthenticationManagerRepo.save(signupAuthenticationManager)

        // 이메일 인증토큰
        // TODO: JWT로 변경하기
        val emailAuthToken = "asdasd123"

        return CustomResponseEntity(data = emailAuthToken)
    }
}