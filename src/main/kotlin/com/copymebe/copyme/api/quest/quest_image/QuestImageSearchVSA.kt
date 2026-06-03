package com.copymebe.copyme.api.quest.quest_image

import com.copymebe.copyme.core.domain.quest.quest_image.QuestImageRepo
import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImage
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import com.copymebe.copyme.core.global.http.swagger.SwaggerSecurityConst
import com.copymebe.copyme.core.global.pagination.OffsetPage
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class QuestImageSearchRequest(
    @Schema(description = "페이지 인덱스", defaultValue = "0")
    val page: Int,

    @Schema(description = "페이지 사이즈", defaultValue = "10")
    val size: Int,
)

@SecurityRequirement(name = SwaggerSecurityConst.BEARER_AUTH)
@Tag(name = "Quest")
@RestController
class QuestImageSearchVSA(
    private val questImageRepo: QuestImageRepo
) {
    @Operation(summary = "퀘스트 이미지 검색")
    @GetMapping("/quest-images")
    fun search(@Valid req: QuestImageSearchRequest): CustomResponseEntity<OffsetPage<List<QuestImage>>> {
        val pageable = PageRequest.of(req.page, req.size)

        val r = questImageRepo.findAll(pageable)
            .let { OffsetPage.create(it) }

        return CustomResponseEntity(data = r)
    }
}