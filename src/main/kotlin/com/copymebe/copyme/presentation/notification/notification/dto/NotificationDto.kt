package com.copymebe.copyme.presentation.notification.notification.dto

import com.copymebe.copyme.core.domain.notification.notification.Notification
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class NotificationDto(
    @Schema(
        description = "Notification ID",
        required = true
    )
    val id: UUID,

    @Schema(
        description = "Member ID",
        required = true
    )
    val memberId: UUID,

    @Schema(
        description = "Title",
        required = true
    )
    val title: String,

    @Schema(
        description = "Description",
        required = true
    )
    val description: String,

    @Schema(
        description = "Event Key",
        required = true
    )
    val eventKey: String,

    @Schema(
        description = "Payload (JSON String)",
        required = true
    )
    val payload: String,

    @Schema(
        description = "읽음 여부",
        required = true
    )
    val isRead: Boolean,
) {
    companion object {
        fun fromEntity(e: Notification): NotificationDto {
            return NotificationDto(
                id = e.id,
                memberId = e.memberId,
                title = e.title,
                description = e.description,
                eventKey = e.eventKey,
                payload = e.payload,
                isRead = e.isRead,
            )
        }
    }
}
