package com.copymebe.copyme.core.global.system

import com.copymebe.copyme.core.domain.quest.quest_image.QuestImageRepo
import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImage
import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImageCategory
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/*
 * QuestImageSet 등록 서비스
 */
@Service
class QuestImagesRegisterFromCsvService(
    @Value("classpath:quest-images.csv")
    private val csv: Resource,
    private val questImageRepo: QuestImageRepo
) {
    /*
     * csv를 읽어와, DB에 일괄 등록
     */
    fun process() {
        // CSV 파일 읽은 후, 엔티티로 변환
        val questImages = csv.useReader { reader ->
            val csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreSurroundingSpaces(true)
                .get()

            val csvParser = CSVParser.builder()
                .setReader(reader)
                .setFormat(csvFormat)
                .get()

            // CSV를 엔티티로 변환
            csvParser.map { record ->
                val category = record.get("category").uppercase()
                val imageUrl = record.get("image_url")

                QuestImage.create(
                    category = QuestImageCategory.valueOf(category),
                    imageUrl = imageUrl
                )
            }
        }

        // 일괄 저장
        questImageRepo.saveAll(questImages)
    }
}

private fun <T> Resource.useReader(
    block: (BufferedReader) -> T
): T {
    return this.inputStream.use { inputStream ->
        InputStreamReader(inputStream, StandardCharsets.UTF_8)
            .buffered()
            .use(block)
    }
}