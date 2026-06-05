package com.copymebe.copyme.core.domain.notification.schedule

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalTime
import java.util.*

interface NotificationScheduleRepo : JpaRepository<NotificationSchedule, UUID> {
    fun findByMemberIdAndTime(memberId: UUID, time: LocalTime): NotificationSchedule?
    fun findAllByTime(time: LocalTime): List<NotificationSchedule>
    fun findAllByMemberId(memberId: UUID, pageable: org.springframework.data.domain.Pageable): org.springframework.data.domain.Page<NotificationSchedule>
}
