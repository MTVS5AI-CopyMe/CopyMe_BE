package com.copymebe.copyme.api.quest.quest_image.dto

import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImage
import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImageCategory
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class QuestImageDto(
    @Schema(description = "Quest Image ID")
    val id: UUID,

    @Schema(description = "Quest Image URL")
    val imageUrl: String,

    @Schema(description = "Quest Image Category")
    val category: QuestImageCategory,

    @Schema(description = "Quest Image Tags")
    val tags: List<String>
) {
    companion object {
        fun fromEntity(e: QuestImage): QuestImageDto {
            return QuestImageDto(
                id = e.id,
                imageUrl = e.imageUrl,
                category = e.category,
                tags = e.tags
            )
        }
    }
}