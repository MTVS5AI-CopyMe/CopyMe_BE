package com.copymebe.copyme.resources.member.signup

import com.copymebe.copyme.core.domain.auth.MaxSignupAuthRequestExceededException
import com.copymebe.copyme.core.domain.auth.SignupAuthenticationManagerRepo
import com.copymebe.copyme.core.domain.auth.models.SignupAuthenticationManager
import com.copymebe.copyme.core.domain.member.AlreadyExistsMemberException
import com.copymebe.copyme.core.domain.member.MemberRepo
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.ApiExceptions
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class MemberSignupAuthCodeRequest(
    @field:Email
    @Schema(
        description = "이메일",
        example = "user@example.com"
    )
    val email: String,
)

@Tag(name = "Member")
@RestController
class MemberSignupAuthCodeRequestController(
    private val memberRepo: MemberRepo,
    private val signupAuthenticationManagerRepo: SignupAuthenticationManagerRepo,
) {
    @Operation(summary = "멤버 회원가입 인증코드 요청")
    @ApiResponse(responseCode = "200")
    @ApiExceptions(
        AlreadyExistsMemberException::class,
        MaxSignupAuthRequestExceededException::class,
    )
    @PostMapping("/members/signup/authcode")
    fun requestSignupAuthCode(
        @RequestBody @Valid req: MemberSignupAuthCodeRequest
    ): CustomResponseEntity<Boolean> {
        val email = req.email

        // 이미 가입된 회원인지 확인
        memberRepo
            .findByEmail(email)
            ?.let {
                throw AlreadyExistsMemberException()
            }

        // 회원가입 인증 매니저 불러오기
        val signupAuthenticationManager =
            signupAuthenticationManagerRepo
                .findByEmail(email)
                ?: SignupAuthenticationManager.create(email)

        // 인증 매니저에 인증요청 추가
        val newAuthCodeRequest = signupAuthenticationManager.addRequestOrThrow()
        println(newAuthCodeRequest.authCode)
        // TODO: 이메일로 AuthCode 전송하기

        // 저장
        signupAuthenticationManagerRepo.save(signupAuthenticationManager)

        return CustomResponseEntity(data = true)
    }
}