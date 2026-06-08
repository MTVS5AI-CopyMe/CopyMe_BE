package com.copymebe.copyme.core.domain.notification.schedule

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalTime
import java.util.*

interface NotificationScheduleRepo : JpaRepository<NotificationSchedule, UUID> {
    fun findByMemberIdAndTime(memberId: UUID, time: LocalTime): NotificationSchedule?
    fun findAllByTime(time: LocalTime): List<NotificationSchedule>
    fun findAllByMemberIdOrderByTimeAsc(
        memberId: UUID,
        pageable: Pageable
    ): Page<NotificationSchedule>
}
