package com.copymebe.copyme.core.domain.auth.models

import com.copymebe.copyme.core.domain.auth.MaxSignupAuthRequestExceeded
import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime
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

    fun addRequestOrThrow(
        authCode: String,
        expiredAt: LocalDateTime,
    ) {
        if (requests.size >= MAX_REQUEST_COUNT) {
            throw MaxSignupAuthRequestExceeded()
        }

        requests.add(
            SignupAuthenticationManagerRequest.create(
                authCode = authCode,
                expiredAt = expiredAt,
                manager = this
            )
        )
    }

    fun removeRequest(requestId: UUID) {
        requests.removeIf { it.id == requestId }
    }

}