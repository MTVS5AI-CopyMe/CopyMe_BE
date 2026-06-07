package com.copymebe.copyme.presentation.notification.schedule.dto

import com.copymebe.copyme.core.domain.notification.schedule.NotificationSchedule
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalTime
import java.util.*

data class NotificationScheduleDto(
    @Schema(
        description = "Notification Schedule ID",
        required = true
    )
    val id: UUID,

    @Schema(
        description = "Member ID",
        required = true
    )
    val memberId: UUID,

    @Schema(
        description = "Notification Time",
        example = "09:30",
        required = true
    )
    @JsonFormat(pattern = "HH:mm")
    val time: LocalTime,
) {
    companion object {
        fun fromEntity(e: NotificationSchedule): NotificationScheduleDto {
            return NotificationScheduleDto(
                id = e.id,
                memberId = e.memberId,
                time = e.time,
            )
        }
    }
}
