package com.copymebe.copyme.presentation.quest.quest_answer

import com.copymebe.copyme.core.domain.quest.quest_answer.QuestAnswerRepo
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.pagination.OffsetPage
import com.copymebe.copyme.presentation.quest.quest_answer.dto.QuestAnswerDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.hibernate.validator.constraints.Range
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class QuestAnswerSearchRequest(
    @Schema(description = "페이지 인덱스", defaultValue = "0")
    @field:Range(min = 0, max = 9999)
    val page: Int,

    @Schema(description = "페이지 사이즈", defaultValue = "10")
    @field:Range(min = 1, max = 9999)
    val size: Int,

    @Schema(
        description = "유저 ID",
        required = false,
        example = "123e4567-e89b-12d3-a456-426614174000"
    )
    val memberId: String? = null
)

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Quest")
@RestController
class QuestAnswerSearchVSA(
    private val questAnswerRepo: QuestAnswerRepo
) {
    @Operation(summary = "퀘스트 응답 검색")
    @GetMapping("/api/v1/quest-answers")
    fun search(@ParameterObject @Valid req: QuestAnswerSearchRequest): CustomResponseEntity<OffsetPage<List<QuestAnswerDto>>> {
        val pageable = PageRequest.of(req.page, req.size)

        val r = when {
            req.memberId != null ->
                questAnswerRepo.findAllByMemberIdAndDeletedAtNull(
                    memberId = UUID.fromString(req.memberId),
                    pageable = pageable
                )

            else ->
                questAnswerRepo.findAllByDeletedAtNull(pageable)
        }.map(QuestAnswerDto::fromEntity)
            .let { OffsetPage.create(it) }

        return CustomResponseEntity(data = r)
    }
}