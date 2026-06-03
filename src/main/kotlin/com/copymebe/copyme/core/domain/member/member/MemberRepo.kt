package com.copymebe.copyme.core.domain.member.member

import com.copymebe.copyme.core.domain.member.member.models.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepo : JpaRepository<Member, UUID> {
    fun findByEmail(email: String): Member?
    fun findByEmailAndDeletedAtNull(email: String): Member?
    fun findByProfileNickname(nickname: String): Member?

    fun findByDevicesRefreshToken(refreshToken: String): Member?

}