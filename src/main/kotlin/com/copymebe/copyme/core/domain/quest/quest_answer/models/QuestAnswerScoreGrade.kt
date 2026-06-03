package com.copymebe.copyme.core.domain.quest.quest_answer.models

enum class QuestAnswerScoreGrade {
    S, A, B, C;

    companion object {
        fun fromScore(score: Int): QuestAnswerScoreGrade {
            return when (score) {
                in 90..100 -> S
                in 70..89 -> A
                in 50..69 -> B
                else -> C // 50 미만 (49 이하)
            }
        }
    }
}
