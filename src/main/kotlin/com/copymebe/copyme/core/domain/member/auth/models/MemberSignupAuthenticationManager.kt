package com.copymebe.copyme.core.domain.member.auth.models

import com.copymebe.copyme.core.domain.base.BaseEntity
import com.copymebe.copyme.core.domain.member.auth.MaxSignupAuthRequestExceededException
import com.copymebe.copyme.core.domain.member.auth.MemberSignupAuthCodeExpiredException
import jakarta.persistence.*

@Entity
@Table(name = "member_signup_authentication_manager")
class MemberSignupAuthenticationManager protected constructor(
    @Column(name = "email", nullable = false)
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
        try {
            requests
                .find { it.authCode == authCode }
                ?.let { request ->
                    // 인증요청이 만료되었다면 throw
                    if (request.isExpired()) {
                        throw MemberSignupAuthCodeExpiredException()
                    }

                    // 인증완료시 모든 인증요청 제거
                    requests.clear()
                }
                ?: throw MemberSignupAuthCodeExpiredException()
        } finally {
            initExpiredRequests()
        }
    }

    /**
     * 만료된 인증요청 초기화
     */
    fun initExpiredRequests() {
        requests.removeIf { it.isExpired() }
    }
}