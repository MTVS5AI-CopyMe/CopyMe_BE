package com.copymebe.copyme.core.domain.auth.models

import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

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
        fun create(
            authCode: String,
            expiredAt: LocalDateTime,
            manager: SignupAuthenticationManager
        ): SignupAuthenticationManagerRequest {
            return SignupAuthenticationManagerRequest(
                authCode = authCode,
                expiredAt = expiredAt,
                manager = manager
            )
        }
    }
}