package com.copymebe.copyme.core.global.security

import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.domain.member.member.NotFoundMemberException
import com.copymebe.copyme.core.domain.member.member.models.UserRole
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
        val member = memberRepo.findByEmailAndDeletedAtNull(username)
            ?: throw NotFoundMemberException()

        val roles = listOf(object : GrantedAuthority {
            override fun getAuthority(): String {
                return UserRole.MEMBER.name
            }
        })

        return User(
            member.email,
            "",
            roles
        )
    }

}