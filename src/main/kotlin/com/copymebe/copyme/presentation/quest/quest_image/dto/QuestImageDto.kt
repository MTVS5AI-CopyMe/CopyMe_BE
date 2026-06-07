package com.copymebe.copyme.presentation.quest.quest_image.dto

import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImage
import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImageCategory
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class QuestImageDto(
    @Schema(
        description = "Quest Image ID",
        required = true
    )
    val id: UUID,

    @Schema(
        description = "Quest Image URL",
        required = true
    )
    val imageUrl: String,

    @Schema(
        description = "Quest Image Category",
        required = true
    )
    val category: QuestImageCategory,

    @Schema(
        description = "Quest Image Tags",
        required = true
    )
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