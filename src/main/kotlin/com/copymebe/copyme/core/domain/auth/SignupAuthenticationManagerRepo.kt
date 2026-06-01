package com.copymebe.copyme.core.domain.auth

import com.copymebe.copyme.core.domain.auth.models.SignupAuthenticationManager
import com.copymebe.copyme.core.domain.member.models.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SignupAuthenticationManagerRepo : JpaRepository<SignupAuthenticationManager, UUID> {
    fun findByEmail(email: String): SignupAuthenticationManager?
}