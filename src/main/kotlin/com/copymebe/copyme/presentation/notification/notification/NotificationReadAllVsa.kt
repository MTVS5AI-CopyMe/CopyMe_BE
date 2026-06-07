package com.copymebe.copyme.presentation.notification.notification

import com.copymebe.copyme.core.domain.notification.notification.NotificationRepo
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.security.getUserId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RestController

class NotificationReadAllResponse(
    @Schema(
        description = "데이터",
        required = true
    )
    override val data: Boolean
) : CustomResponseEntity<Boolean>()

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Notification")
@RestController
class NotificationReadAllVsa(
    private val notificationRepo: NotificationRepo,
) {
    @Operation(summary = "알림 전체 읽음 처리")
    @PatchMapping("/api/v1/notifications/read-all")
    @Transactional
    fun read(
        authentication: Authentication,
    ): NotificationReadAllResponse {
        val userId = authentication.getUserId()

        val notifications = notificationRepo.findAllByMemberIdAndIsReadFalse(userId)
        notifications.forEach { it.read() }
        notificationRepo.saveAll(notifications)

        return NotificationReadAllResponse(data = true)
    }
}
