package com.copymebe.copyme.core.domain.quest.quest_answer.models

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class QuestAnswerScore protected constructor(
    @Column(name = "score")
    var score: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "score_grade")
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
