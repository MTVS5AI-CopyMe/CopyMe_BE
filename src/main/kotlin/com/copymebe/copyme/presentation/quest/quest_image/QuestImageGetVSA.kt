package com.copymebe.copyme.presentation.quest.quest_image

import com.copymebe.copyme.core.domain.quest.quest_image.QuestImageRepo
import com.copymebe.copyme.core.global.exception.CustomBadRequestException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.presentation.quest.quest_image.dto.QuestImageDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

class QuestImageReadResponse(
    @Schema(
        description = "데이터",
        required = true
    )
    override val data: QuestImageDto
) : CustomResponseEntity<QuestImageDto>()

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Quest")
@RestController
class QuestImageGetVSA(
    private val questImageRepo: QuestImageRepo
) {
    @Operation(summary = "퀘스트 이미지 단건 조회")
    @GetMapping("/api/v1/quest-images/{id}")
    fun read(
        @PathVariable id: UUID
    ): QuestImageReadResponse {
        val questImage = questImageRepo.findByIdOrNull(id)
            ?: throw CustomBadRequestException("존재하지 않는 퀘스트 이미지 ID")

        return QuestImageReadResponse(
            data = QuestImageDto.fromEntity(questImage)
        )
    }
}
