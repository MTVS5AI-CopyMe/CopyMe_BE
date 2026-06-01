package com.copymebe.copyme.core.domain.member.auth

import com.copymebe.copyme.core.domain.member.auth.models.MemberSignupAuthenticationManager
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberSignupAuthenticationManagerRepo : JpaRepository<MemberSignupAuthenticationManager, UUID> {
    fun findByEmail(email: String): MemberSignupAuthenticationManager?
}