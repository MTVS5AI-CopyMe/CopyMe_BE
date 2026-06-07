package com.copymebe.copyme.presentation.notification.notification

import com.copymebe.copyme.core.domain.notification.notification.NotificationRepo
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.pagination.OffsetPage
import com.copymebe.copyme.core.global.security.getUserId
import com.copymebe.copyme.presentation.notification.notification.dto.NotificationDto
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

data class NotificationSearchRequest(
    @Schema(description = "페이지 인덱스", defaultValue = "0")
    @field:Range(min = 0, max = 9999)
    val page: Int,

    @Schema(description = "페이지 크기", defaultValue = "10")
    @field:Range(min = 1, max = 9999)
    val size: Int,
)

class NotificationSearchResponse(
    @Schema(
        description = "데이터",
        required = true
    )
    override val data: OffsetPage<List<NotificationDto>>
) : CustomResponseEntity<OffsetPage<List<NotificationDto>>>()

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Notification")
@RestController
class NotificationSearchVSA(
    private val notificationRepo: NotificationRepo,
) {
    @Operation(summary = "알림 검색")
    @GetMapping("/api/v1/notifications")
    fun search(
        authentication: Authentication,
        @ParameterObject @Valid req: NotificationSearchRequest,
    ): NotificationSearchResponse {
        val userId = authentication.getUserId()
        val pageable = PageRequest.of(req.page, req.size)

        val r = notificationRepo.findAllByMemberId(
            memberId = userId,
            pageable = pageable
        ).map(NotificationDto::fromEntity)
            .let { OffsetPage.create(it) }

        return NotificationSearchResponse(data = r)
    }
}
