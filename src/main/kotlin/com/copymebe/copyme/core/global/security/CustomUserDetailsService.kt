package com.copymebe.copyme.core.global.security

import com.copymebe.copyme.core.domain.member.MemberRepo
import com.copymebe.copyme.core.domain.member.NotFoundMemberException
import com.copymebe.copyme.core.domain.member.models.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepo: MemberRepo
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepo.findByEmail(username)
            ?: throw NotFoundMemberException()

        val roles = listOf(object : GrantedAuthority {
            override fun getAuthority(): String {
                return UserRole.MEMBER.name
            }
        })

        return User(
            member.email,
            member.password,
            roles
        )
    }

}