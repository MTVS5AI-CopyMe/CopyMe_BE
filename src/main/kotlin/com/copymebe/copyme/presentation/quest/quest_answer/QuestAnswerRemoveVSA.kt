package com.copymebe.copyme.presentation.quest.quest_answer

import com.copymebe.copyme.core.domain.quest.quest_answer.QuestAnswerRepo
import com.copymebe.copyme.core.global.exception.CustomBadRequestException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.security.CustomForbiddenException
import com.copymebe.copyme.core.global.security.getUserId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*


@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Quest")
@RestController
class QuestAnswerRemoveVSA(
    private val questAnswerRepo: QuestAnswerRepo
) {
    @Operation(summary = "퀘스트 응답 삭제")
    @DeleteMapping("/api/v1/quest-answers/{questId}")
    fun remove(
        authentication: Authentication,
        @PathVariable("questId") questId: UUID,
    ): CustomResponseEntity<UUID> {
        val userId = authentication.getUserId()

        val questAnswer = questAnswerRepo.findByIdOrNull(questId)
            ?: throw CustomBadRequestException("존재하지 않는 퀘스트 응답 ID")

        // 작성자 일치 확인
        if (questAnswer.isOwner(userId).not()) {
            throw CustomForbiddenException()
        }

        // 삭제 처리
        questAnswer.softDelete()

        // 저장
        questAnswerRepo.save(questAnswer)

        return CustomResponseEntity(data = questAnswer.id)
    }
}