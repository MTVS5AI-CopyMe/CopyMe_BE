package com.copymebe.copyme.core.domain.quest.quest_answer.models

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class QuestAnswerScore protected constructor(
    @Column(name = "score", nullable = false)
    var score: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "score_grade", nullable = false)
    var scoreGrade: QuestAnswerScoreGrade
) {
    companion object {
        fun create(
            score: Int,
        ): QuestAnswerScore {

            return QuestAnswerScore(
                score = score,
                scoreGrade = QuestAnswerScoreGrade.fromScore(score),
            )
        }
    }
}
