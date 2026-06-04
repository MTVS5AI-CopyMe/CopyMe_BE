package com.copymebe.copyme.presentation.quest.quest_answer

import com.copymebe.copyme.core.domain.quest.quest_answer.QuestAnswerRepo
import com.copymebe.copyme.core.domain.quest.quest_answer.models.QuestAnswer
import com.copymebe.copyme.core.domain.quest.quest_image.QuestImageRepo
import com.copymebe.copyme.core.global.exception.CustomBadRequestException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.security.getUserId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.URL
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

data class QuestAnswerCreateRequest(
    @Schema(
        description = "퀘스트 이미지 ID",
        example = "019e8d4b-e50b-7c38-b4f1-382ec3e7cc7d"
    )
    @field:NotEmpty
    val questImageId: UUID,

    @Schema(
        description = "응답 이미지",
        example = "https://picsum.photos/seed/picsum/200/300"
    )
    @field:URL
    val answerImageUrl: String,
)

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Quest")
@RestController
class QuestAnswerCreateVSA(
    private val questImageRepo: QuestImageRepo,
    private val questAnswerRepo: QuestAnswerRepo
) {
    @Operation(summary = "퀘스트 응답 생성")
    @PostMapping("/api/v1/quest-answers")
    fun create(
        authentication: Authentication,
        @RequestBody @Valid req: QuestAnswerCreateRequest
    ): CustomResponseEntity<UUID> {
        val userId = authentication.getUserId()

        // 퀘스트 이미지 불러오기
        val questImage = this.questImageRepo.findByIdOrNull(req.questImageId)
            ?: throw CustomBadRequestException("존재하지 않는 퀘스트 이미지 ID")

        // TODO: 유사도 계산 API 호출
        val score = 10

        // 응답 생성
        val questAnswer = QuestAnswer.create(
            memberId = userId,
            questImage = questImage,
            answerImageUrl = req.answerImageUrl,
            score = score,
        )

        // 저장
        questAnswerRepo.save(questAnswer)

        return CustomResponseEntity(data = questAnswer.id)
    }
}