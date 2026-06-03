package com.copymebe.copyme.core.domain.quest.quest_answer.models

import com.copymebe.copyme.core.domain.base.BaseEntity
import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImage
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "quest_answer")
class QuestAnswer protected constructor(
    @Column(name = "member_id", nullable = false)
    val memberId: UUID,

    @ManyToOne
    @JoinColumn(name = "quest_image_id", nullable = false)
    val questImage: QuestImage,

    @Column(name = "answer_image_url", nullable = false)
    var answerImageUrl: String,

    @Embedded
    var score: QuestAnswerScore
) : BaseEntity() {
    companion object {
        fun create(
            memberId: UUID,
            questImage: QuestImage,
            answerImageUrl: String,
            score: Int
        ): QuestAnswer {
            return QuestAnswer(
                memberId = memberId,
                questImage = questImage,
                answerImageUrl = answerImageUrl,
                score = QuestAnswerScore.create(score)
            )
        }
    }

    fun isOwner(memberId: UUID): Boolean {
        return this.memberId == memberId
    }
}
