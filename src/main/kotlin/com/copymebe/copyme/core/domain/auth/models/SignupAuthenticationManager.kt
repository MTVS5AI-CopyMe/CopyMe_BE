package com.copymebe.copyme.core.domain.auth.models

import com.copymebe.copyme.core.domain.auth.MaxSignupAuthRequestExceededException
import com.copymebe.copyme.core.domain.auth.SignupAuthCodeExpiredException
import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "signup_authentication_manager")
class SignupAuthenticationManager protected constructor(
    @Column(name = "email", nullable = false)
    var email: String,

    @OneToMany(
        mappedBy = "manager",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val requests: MutableList<SignupAuthenticationManagerRequest> = mutableListOf(),
) : BaseEntity() {
    companion object {
        const val MAX_REQUEST_COUNT = 3

        fun create(
            email: String,
        ): SignupAuthenticationManager {
            return SignupAuthenticationManager(
                email = email,
            )
        }
    }

    /**
     * 인증요청 추가
     */
    fun addRequestOrThrow(
    ): SignupAuthenticationManagerRequest {
        // 만료된 인증요청 제거
        initRequests()

        // 최대 인증요청 수 초과 체크
        if (requests.size >= MAX_REQUEST_COUNT) {
            throw MaxSignupAuthRequestExceededException()
        }

        return SignupAuthenticationManagerRequest
            .create(manager = this)
            .also { newRequest ->
                // 인증요청 추가
                requests.add(newRequest)
            }
    }

    /**
     * 인증요청 제거
     */
    fun removeRequest(requestId: UUID) {
        requests.removeIf { it.id == requestId }
    }

    /**
     * 인증
     */
    fun authenticate(authCode: String): Boolean {
        return try {
            requests
                .find { it.authCode == authCode }
                ?.let { request ->
                    // 인증요청이 만료되었다면 throw
                    if (request.isExpired()) {
                        throw SignupAuthCodeExpiredException()
                    }

                    // 인증완료시 해당 요청 제거
                    removeRequest(request.id)
                    true
                }
                ?: false
        } finally {
            initRequests()
        }
    }

    /**
     * 만료된 인증요청 초기화
     */
    fun initRequests() {
        requests.removeIf { it.isExpired() }
    }
}