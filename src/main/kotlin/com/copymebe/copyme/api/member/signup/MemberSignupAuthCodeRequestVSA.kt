package com.copymebe.copyme.api.member.signup

import com.copymebe.copyme.core.domain.member.auth.MaxSignupAuthRequestExceededException
import com.copymebe.copyme.core.domain.member.auth.MemberSignupAuthenticationManagerRepo
import com.copymebe.copyme.core.domain.member.auth.models.MemberSignupAuthenticationManager
import com.copymebe.copyme.core.domain.member.member.AlreadyExistsMemberException
import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.CustomApiExceptions
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
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

@Tag(name = "Member Signup")
@RestController
class MemberSignupAuthCodeRequestVSA(
    private val memberRepo: MemberRepo,
    private val memberSignupAuthenticationManagerRepo: MemberSignupAuthenticationManagerRepo,
) {
    @Operation(summary = "멤버 회원가입 인증코드 요청")
    @CustomApiExceptions(
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
        val memberSignupAuthenticationManager =
            memberSignupAuthenticationManagerRepo
                .findByEmail(email)
                ?: MemberSignupAuthenticationManager.create(email)

        // 인증 매니저에 인증요청 추가
        val newAuthCodeRequest = memberSignupAuthenticationManager.addRequestOrThrow()
        // TODO: 이메일로 AuthCode 전송하기
        println(newAuthCodeRequest.authCode)

        // 저장
        memberSignupAuthenticationManagerRepo.save(memberSignupAuthenticationManager)

        return CustomResponseEntity(data = true)
    }
}