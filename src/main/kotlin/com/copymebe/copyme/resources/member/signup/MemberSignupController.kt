package com.copymebe.copyme.resources.member.signup

import com.copymebe.copyme.core.domain.member.auth.MemberSignupEmailTokenInvalidException
import com.copymebe.copyme.core.domain.member.auth.services.MemberSignupAuthTokenProvider
import com.copymebe.copyme.core.domain.member.member.AlreadyExistsMemberException
import com.copymebe.copyme.core.domain.member.member.AlreadyExistsMemberNicknameException
import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.domain.member.member.models.Member
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.ApiExceptions
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.URL
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class MemberSignupRequest(
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

    @field:URL
    @Schema(
        description = "프로필 이미지 URL",
        example = "https://picsum.photos/seed/picsum/200/300"
    )
    val profileImageUrl: String,

    @field:NotEmpty
    @Schema(
        description = "닉네임",
        example = "asd"
    )
    val nickname: String,

    @field:NotEmpty
    @Schema(
        description = "이메일 인증토큰",
        example = "asd123"
    )
    val emailAuthToken: String,
)

@Tag(name = "Member Signup")
@RestController
class MemberSignupController(
    private val memberRepo: MemberRepo,
    private val passwordEncoder: PasswordEncoder,
    private val memberSignupAuthTokenProvider: MemberSignupAuthTokenProvider,
) {
    @Operation(summary = "멤버 회원가입")
    @ApiExceptions(
        AlreadyExistsMemberException::class,
        MemberSignupEmailTokenInvalidException::class,
        AlreadyExistsMemberNicknameException::class,
    )
    @PostMapping("/members/signup")
    fun signup(
        @RequestBody @Valid req: MemberSignupRequest
    ): CustomResponseEntity<UUID> {
        // EmailAuthToken 검증
        memberSignupAuthTokenProvider
            .parseEmailOrThrow(req.emailAuthToken)
            .let { parsedEmail ->
                if (parsedEmail != req.email) {
                    throw MemberSignupEmailTokenInvalidException()
                }
            }


        // 이미 가입된 회원인지 확인
        memberRepo.run {
            findByEmail(req.email)?.let {
                throw AlreadyExistsMemberException()
            }
            findByProfileNickname(req.nickname)?.let {
                throw AlreadyExistsMemberNicknameException()
            }
        }

        // 신규 멤버 생성
        val newMember = Member.create(
            email = req.email,
            password = passwordEncoder.encode(req.password)!!,
            profileImageUrl = req.profileImageUrl,
            nickname = req.nickname,
        )

        // 저장
        memberRepo.save(newMember)

        return CustomResponseEntity(data = newMember.id)
    }
}