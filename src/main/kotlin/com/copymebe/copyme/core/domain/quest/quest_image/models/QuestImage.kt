package com.copymebe.copyme.core.domain.quest.quest_image.models

import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "quest_image")
class QuestImage protected constructor(
    @Column(name = "image_url")
    var imageUrl: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    var category: QuestImageCategory,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description")
    var tags: MutableList<String> = mutableListOf()
) : BaseEntity() {
    companion object {
        fun create(
            imageUrl: String,
            category: QuestImageCategory,
        ): QuestImage {
            return QuestImage(
                imageUrl = imageUrl,
                category = category,
            )
        }
    }
}
