package com.copymebe.copyme.presentation.quest.quest_answer.dto

import com.copymebe.copyme.core.domain.quest.quest_answer.models.QuestAnswer
import com.copymebe.copyme.core.domain.quest.quest_answer.models.QuestAnswerScoreGrade
import com.copymebe.copyme.presentation.quest.quest_image.dto.QuestImageDto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

data class QuestAnswerDto(
    @Schema(description = "ID")
    val id: UUID,

    @Schema(description = "Member ID")
    val memberId: UUID,

    @Schema(description = "Quest Image")
    val questImage: QuestImageDto,

    @Schema(description = "Answer Image Url")
    val answerImageUrl: String,

    @Schema(description = "Score")
    val score: Int,

    @Schema(description = "Score Grade")
    val scoreGrade: QuestAnswerScoreGrade,

    @Schema(description = "생성일시")
    val createdAt: LocalDateTime,

    @Schema(description = "수정일시")
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(e: QuestAnswer): QuestAnswerDto {
            return QuestAnswerDto(
                id = e.id,
                memberId = e.memberId,
                questImage = QuestImageDto.fromEntity(e.questImage),
                answerImageUrl = e.answerImageUrl,
                score = e.score.score,
                scoreGrade = e.score.scoreGrade,
                createdAt = e.createdAt,
                updatedAt = e.updatedAt,
            )
        }
    }
}