package com.copymebe.copyme.core.domain.quest.quest_image

import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImage
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface QuestImageRepo : JpaRepository<QuestImage, UUID>