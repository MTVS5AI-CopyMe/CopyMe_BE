package com.copymebe.copyme.core.domain.member

import com.copymebe.copyme.core.domain.member.models.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepo : JpaRepository<Member, UUID> {
    fun findByEmail(email: String): Member?
    fun findByProfileNickname(nickname: String): Member?

    fun findAllByEmail(email: String, page: Pageable): Page<Member>
}