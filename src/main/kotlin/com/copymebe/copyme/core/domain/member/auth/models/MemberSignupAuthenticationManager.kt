package com.copymebe.copyme.core.domain.member.auth.models

import com.copymebe.copyme.core.domain.base.BaseEntity
import com.copymebe.copyme.core.domain.member.auth.MaxSignupAuthRequestExceededException
import com.copymebe.copyme.core.domain.member.auth.MemberSignupAuthCodeInvalidException
import jakarta.persistence.*

@Entity
@Table(name = "member_signup_authentication_manager")
class MemberSignupAuthenticationManager protected constructor(
    @Column(name = "email")
    var email: String,

    @OneToMany(
        mappedBy = "manager",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val requests: MutableList<MemberSignupAuthenticationManagerRequest> = mutableListOf(),
) : BaseEntity() {
    companion object {
        const val MAX_REQUEST_COUNT = 3

        fun create(
            email: String,
        ): MemberSignupAuthenticationManager {
            return MemberSignupAuthenticationManager(
                email = email,
            )
        }
    }

    /**
     * 인증요청 추가
     */
    fun addRequestOrThrow(
    ): MemberSignupAuthenticationManagerRequest {
        // 만료된 인증요청 제거
        initExpiredRequests()

        // 최대 인증요청 수 초과 체크
        if (requests.size >= MAX_REQUEST_COUNT) {
            throw MaxSignupAuthRequestExceededException()
        }

        return MemberSignupAuthenticationManagerRequest
            .create(manager = this)
            .also { newRequest ->
                // 인증요청 추가
                requests.add(newRequest)
            }
    }

    /**
     * 인증
     */
    fun authenticateOrThrow(authCode: String) {
        initExpiredRequests()

        requests
            .find { (it.authCode == authCode) and it.isExpired().not() }
            ?.let {
                // 인증완료시 모든 인증요청 제거
                requests.clear()
            }
            ?: throw MemberSignupAuthCodeInvalidException()
    }

    /**
     * 만료된 인증요청 초기화
     */
    fun initExpiredRequests() {
        requests.removeIf { it.isExpired() }
    }
}