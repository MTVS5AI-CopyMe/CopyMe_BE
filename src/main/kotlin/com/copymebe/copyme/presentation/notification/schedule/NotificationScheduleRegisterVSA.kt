package com.copymebe.copyme.presentation.notification.schedule

import com.copymebe.copyme.core.domain.notification.schedule.NotificationSchedule
import com.copymebe.copyme.core.domain.notification.schedule.NotificationScheduleRepo
import com.copymebe.copyme.core.global.exception.CustomBadRequestException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.CustomApiExceptions
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.security.getUserId
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime
import java.util.*

data class RegisterNotificationScheduleRequest(
    @Schema(
        description = "Notification time",
        example = "09:30"
    )
    @JsonFormat(pattern = "HH:mm")
    @field:NotNull
    val time: LocalTime,
)

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Notification")
@RestController
class NotificationScheduleRegisterVSA(
    private val notificationScheduleRepo: NotificationScheduleRepo,
) {
    @Operation(summary = "알림 예약")
    @CustomApiExceptions(
        CustomBadRequestException::class
    )
    @PostMapping("/api/v1/notification-schedules")
    fun register(
        authentication: Authentication,
        @RequestBody @Valid req: RegisterNotificationScheduleRequest,
    ): CustomResponseEntity<UUID> {
        val memberId = authentication.getUserId()

        // 30분 기준 검사
        if (NotificationSchedule.validateHalfHour(req.time).not()) {
            throw CustomBadRequestException("30분 단위로만 등록만 가능")
        }

        // 알림 스케줄 생성
        val notificationSchedule = notificationScheduleRepo.findByMemberIdAndTime(
            memberId = memberId,
            time = req.time,
        ) ?: NotificationSchedule.create(
            memberId = memberId,
            time = req.time,
        )

        // 저장
        notificationScheduleRepo.save(notificationSchedule)

        return CustomResponseEntity(data = notificationSchedule.id)
    }
}
