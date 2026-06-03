package com.copymebe.copyme.core.global

import com.copymebe.copyme.core.domain.quest.quest_image.QuestImageRepo
import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImage
import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImageCategory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class CustomApplicationRunner(
    private val defaultQuestImageInitializer: DefaultQuestImageInitializer
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        defaultQuestImageInitializer.init()
    }
}

@Component
class DefaultQuestImageInitializer(
    private val questImageRepo: QuestImageRepo
) {
    fun init() {
        if (questImageRepo.count() > 0) {
            return
        }

        // 초기 퀘스트 이미지
        val questImages = listOf(
            QuestImage.create(
                imageUrl = "https://plus.unsplash.com/premium_photo-1671656349322-41de944d259b?q=80",
                category = QuestImageCategory.FACE,
            ),
            QuestImage.create(
                imageUrl = "https://plus.unsplash.com/premium_photo-1671656333460-793292581bc6?q=80",
                category = QuestImageCategory.FACE,
            ),
            QuestImage.create(
                imageUrl = "https://images.unsplash.com/photo-1611695434369-a8f5d76ceb7b?q=80",
                category = QuestImageCategory.FACE,
            ),

            QuestImage.create(
                imageUrl = "https://plus.unsplash.com/premium_photo-1668896123983-00aab3eb5b98?q=80",
                category = QuestImageCategory.POSE_HAND,
            ),
            QuestImage.create(
                imageUrl = "https://images.unsplash.com/photo-1542596594-649edbc13630?q=80",
                category = QuestImageCategory.POSE_HAND,
            ),
            QuestImage.create(
                imageUrl = "https://images.unsplash.com/photo-1621347310235-3670ea823677?q=80",
                category = QuestImageCategory.POSE_HAND,
            ),

            QuestImage.create(
                imageUrl = "https://images.unsplash.com/photo-1622502887577-5a321783c8ae?q=80",
                category = QuestImageCategory.POSE_BODY,
            ),
            QuestImage.create(
                imageUrl = "https://plus.unsplash.com/premium_photo-1674420731071-4817bba52526?q=80",
                category = QuestImageCategory.POSE_BODY,
            ),
            QuestImage.create(
                imageUrl = "https://plus.unsplash.com/premium_photo-1664299879533-07b7f8e9f89d?q=80",
                category = QuestImageCategory.POSE_BODY,
            ),
            QuestImage.create(
                imageUrl = "https://images.unsplash.com/photo-1458312732998-763933ed4896?q=80",
                category = QuestImageCategory.POSE_BODY,
            ),
        )

        questImageRepo.saveAll(questImages)
    }
}