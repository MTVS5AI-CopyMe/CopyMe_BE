package com.copymebe.copyme.core.domain.quest.quest_answer

import com.copymebe.copyme.core.domain.quest.quest_answer.models.QuestAnswer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QuestAnswerRepo : JpaRepository<QuestAnswer, UUID> {
    fun findAllByDeletedAtNull(pageable: Pageable): Page<QuestAnswer>
    fun findAllByMemberIdAndDeletedAtNull(memberId: UUID?, pageable: Pageable): Page<QuestAnswer>
    fun findAllByMemberIdInAndDeletedAtNull(memberIds: List<UUID>): List<QuestAnswer>
}