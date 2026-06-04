package com.copymebe.copyme.api.notification.notification

import com.copymebe.copyme.core.domain.notification.notification.NotificationRepo
import com.copymebe.copyme.core.global.exception.CustomBadRequestException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.security.CustomForbiddenException
import com.copymebe.copyme.core.global.security.getUserId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Notification")
@RestController
class NotificationReadVsa(
    private val notificationRepo: NotificationRepo,
) {
    @Operation(summary = "알림 읽음 처리")
    @PatchMapping("/api/v1/notifications/{notificationId}/read")
    fun read(
        authentication: Authentication,
        @PathVariable notificationId: UUID,
    ): CustomResponseEntity<Boolean> {
        val userId = authentication.getUserId()

        // 알림 찾기
        val notification = notificationRepo.findByIdOrNull(notificationId)
            ?: throw CustomBadRequestException("존재하지 않는 Notification ID")

        // 소유자 확인
        if (notification.isOwner(userId).not()) {
            throw CustomForbiddenException()
        }

        // 알림 읽음 처리
        notification.read()

        // 저장
        notificationRepo.save(notification)

        return CustomResponseEntity(data = true)
    }
}
