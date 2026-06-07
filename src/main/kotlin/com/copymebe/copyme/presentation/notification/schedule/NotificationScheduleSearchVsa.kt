package com.copymebe.copyme.presentation.notification.schedule

import com.copymebe.copyme.core.domain.notification.schedule.NotificationScheduleRepo
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.pagination.OffsetPage
import com.copymebe.copyme.core.global.security.getUserId
import com.copymebe.copyme.presentation.notification.schedule.dto.NotificationScheduleDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.hibernate.validator.constraints.Range
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class NotificationScheduleSearchRequest(
    @Schema(description = "페이지 인덱스", defaultValue = "0")
    @field:Range(min = 0, max = 9999)
    val page: Int,

    @Schema(description = "페이지 크기", defaultValue = "10")
    @field:Range(min = 1, max = 9999)
    val size: Int,
)

class NotificationScheduleSearchResponse(
    @Schema(
        description = "데이터",
        required = true
    )
    override val data: OffsetPage<List<NotificationScheduleDto>>
) : CustomResponseEntity<OffsetPage<List<NotificationScheduleDto>>>()

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Notification")
@RestController
class NotificationScheduleSearchVsa(
    private val notificationScheduleRepo: NotificationScheduleRepo,
) {
    @Operation(summary = "알림 예약 목록 조회")
    @GetMapping("/api/v1/notification-schedules")
    fun search(
        authentication: Authentication,
        @ParameterObject @Valid req: NotificationScheduleSearchRequest,
    ): NotificationScheduleSearchResponse {
        val memberId = authentication.getUserId()
        val pageable = PageRequest.of(req.page, req.size)

        val r = notificationScheduleRepo.findAllByMemberId(
            memberId = memberId,
            pageable = pageable,
        ).map(NotificationScheduleDto::fromEntity)
            .let { OffsetPage.create(it) }

        return NotificationScheduleSearchResponse(data = r)
    }
}
