package com.copymebe.copyme.presentation.quest.quest_answer.dto

import com.copymebe.copyme.core.domain.quest.quest_answer.models.QuestAnswer
import com.copymebe.copyme.core.domain.quest.quest_answer.models.QuestAnswerScoreGrade
import com.copymebe.copyme.presentation.quest.quest_image.dto.QuestImageDto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

data class QuestAnswerDto(
    @Schema(
        description = "ID",
        required = true
    )
    val id: UUID,

    @Schema(
        description = "Member ID",
        required = true
    )
    val memberId: UUID,

    @Schema(
        description = "Quest Image",
        required = true
    )
    val questImage: QuestImageDto,

    @Schema(
        description = "Answer Image Url",
        required = true
    )
    val answerImageUrl: String,

    @Schema(
        description = "Score",
        required = true
    )
    val score: Int,

    @Schema(
        description = "Score Grade",
        required = true
    )
    val scoreGrade: QuestAnswerScoreGrade,

    @Schema(
        description = "생성일시",
        required = true
    )
    val createdAt: LocalDateTime,

    @Schema(
        description = "수정일시",
        required = true
    )
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