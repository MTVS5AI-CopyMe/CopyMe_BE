package com.copymebe.copyme.presentation.member.signup

import com.copymebe.copyme.core.domain.member.auth.MemberSignupEmailTokenInvalidException
import com.copymebe.copyme.core.domain.member.auth.services.MemberSignupAuthTokenProvider
import com.copymebe.copyme.core.domain.member.member.AlreadyExistsMemberException
import com.copymebe.copyme.core.domain.member.member.AlreadyExistsMemberNicknameException
import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.domain.member.member.models.Member
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.CustomApiExceptions
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class MemberSignupRequest(
    @Schema(
        description = "이메일",
        example = "user@example.com"
    )
    @field:Email
    val email: String,

    @field:NotEmpty
    @Schema(
        description = "비밀번호",
        example = "asdasd123!!"
    )
    val password: String,

    @Schema(
        description = "프로필 이미지 URL",
        example = "https://picsum.photos/seed/picsum/200/300"
    )
    @field:URL
    val profileImageUrl: String,

    @Schema(
        description = "닉네임",
        example = "asd"
    )
    @field:NotEmpty
    @field:Size(min = 1, max = 12)
    val nickname: String,

    @Schema(
        description = "이메일 인증토큰",
        example = "asd123"
    )
    @field:NotEmpty
    val emailAuthToken: String,
)

class MemberSignupResponse(
    @Schema(
        description = "데이터",
        required = true
    )
    override val data: UUID
) : CustomResponseEntity<UUID>()

@Tag(name = "Member Signup")
@RestController
class MemberSignupVSA(
    private val memberRepo: MemberRepo,
    private val passwordEncoder: PasswordEncoder,
    private val memberSignupAuthTokenProvider: MemberSignupAuthTokenProvider,
) {
    @Operation(summary = "멤버 회원가입")
    @CustomApiExceptions(
        AlreadyExistsMemberException::class,
        MemberSignupEmailTokenInvalidException::class,
        AlreadyExistsMemberNicknameException::class,
    )
    @PostMapping("/api/v1/members/signup")
    fun signup(
        @RequestBody @Valid req: MemberSignupRequest
    ): MemberSignupResponse {
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

        return MemberSignupResponse(data = newMember.id)
    }
}