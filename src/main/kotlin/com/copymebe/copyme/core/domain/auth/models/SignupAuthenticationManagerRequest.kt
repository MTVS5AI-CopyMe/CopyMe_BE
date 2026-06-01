package com.copymebe.copyme.core.domain.auth.models

import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "signup_authentication_manager_request")
class SignupAuthenticationManagerRequest protected constructor(
    @Column(name = "auth_code", nullable = false)
    var authCode: String,

    @Column(name = "expired_at", nullable = false)
    var expiredAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    val manager: SignupAuthenticationManager
) : BaseEntity() {
    companion object {
        const val EXPIRED_MINUTES: Long = 10

        fun create(
            manager: SignupAuthenticationManager
        ): SignupAuthenticationManagerRequest {
            val authCode = generateAuthCode()
            val expiredAt = LocalDateTime.now().plusMinutes(EXPIRED_MINUTES)

            return SignupAuthenticationManagerRequest(
                authCode = authCode,
                expiredAt = expiredAt,
                manager = manager
            )
        }

        fun generateAuthCode(): String {
            return UUID.randomUUID().toString().substring(0, 6)
        }
    }

    fun isExpired(): Boolean {
        return expiredAt.isBefore(LocalDateTime.now())
    }
}