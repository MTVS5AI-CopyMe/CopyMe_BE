package com.copymebe.copyme.core.domain.notification.notification

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface NotificationRepo : JpaRepository<Notification, UUID> {
    fun findAllByMemberId(memberId: UUID, pageable: Pageable): Page<Notification>
    fun findAllByMemberIdAndIsReadFalse(memberId: UUID): List<Notification>
}
