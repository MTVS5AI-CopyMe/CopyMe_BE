package com.copymebe.copyme.api.notification.notification.dto

import com.copymebe.copyme.core.domain.notification.notification.Notification
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class NotificationDto(
    @Schema(description = "Notification ID")
    val id: UUID,

    @Schema(description = "Member ID")
    val memberId: UUID,

    @Schema(description = "Title")
    val title: String,

    @Schema(description = "Description")
    val description: String,

    @Schema(description = "Event Key")
    val eventKey: String,

    @Schema(description = "Payload")
    val payload: String,

    @Schema(description = "읽음 여부")
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
