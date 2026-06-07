package com.copymebe.copyme.presentation.quest.quest_answer

import com.copymebe.copyme.core.domain.quest.quest_answer.QuestAnswerRepo
import com.copymebe.copyme.core.global.exception.CustomBadRequestException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.presentation.quest.quest_answer.dto.QuestAnswerDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*

class QuestAnswerGetResponse(
    @Schema(
        description = "데이터",
        required = true
    )
    override val data: QuestAnswerDto
) : CustomResponseEntity<QuestAnswerDto>()

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Quest")
@RestController
class QuestAnswerGetVSA(
    private val questAnswerRepo: QuestAnswerRepo
) {
    @Operation(summary = "퀘스트 응답 단건 조회")
    @GetMapping("/api/v1/quest-answers/{questAnswerId}")
    fun get(
        @PathVariable questAnswerId: UUID
    ): QuestAnswerGetResponse {
        val questAnswer = questAnswerRepo.findByIdAndDeletedAtNull(questAnswerId)
            ?: throw CustomBadRequestException("존재하지 않는 퀘스트 응답 ID")

        return QuestAnswerGetResponse(
            data = QuestAnswerDto.fromEntity(questAnswer)
        )
    }
}
