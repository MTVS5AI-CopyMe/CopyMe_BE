package com.copymebe.copyme.core.domain.notification.schedule

import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "notification_schedule")
class NotificationSchedule protected constructor(
    @Column(name = "member_id", nullable = false)
    val memberId: UUID,

    @Column(name = "time", nullable = false)
    var time: LocalTime,
) : BaseEntity()