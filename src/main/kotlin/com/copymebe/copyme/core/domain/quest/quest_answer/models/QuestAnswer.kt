package com.copymebe.copyme.core.domain.quest.quest_answer.models

import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "quest_answer")
class QuestAnswer protected constructor(
    @Column(name = "member_id", nullable = false)
    val memberId: UUID,

    @Column(name = "quest_image_id", nullable = false)
    val questImageId: UUID,

    @Column(name = "answer_image_url", nullable = false)
    var answerImageUrl: String,

    @Embedded
    var score: QuestAnswerScore
) : BaseEntity() {
    companion object {
        fun create(
            memberId: UUID,
            questImageId: UUID,
            answerImageUrl: String,
            score: Int
        ): QuestAnswer {
            return QuestAnswer(
                memberId = memberId,
                questImageId = questImageId,
                answerImageUrl = answerImageUrl,
                score = QuestAnswerScore.create(score)
            )
        }
    }

    fun isOwner(memberId: UUID): Boolean {
        return this.memberId == memberId
    }
}
