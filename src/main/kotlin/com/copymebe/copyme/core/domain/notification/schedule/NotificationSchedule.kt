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
    @Column(name = "member_id")
    val memberId: UUID,

    @Column(name = "time")
    var time: LocalTime,
) : BaseEntity() {
    companion object {
        fun create(
            memberId: UUID,
            time: LocalTime,
        ): NotificationSchedule {
            return NotificationSchedule(
                memberId = memberId,
                time = time,
            )
        }

        /**
         * 30분 단위인지 확인
         */
        fun validateHalfHour(time: LocalTime): Boolean {
            val isValid = (time.minute % 30 != 0 || time.second != 0 || time.nano != 0).not()

            return isValid
        }
    }
}
