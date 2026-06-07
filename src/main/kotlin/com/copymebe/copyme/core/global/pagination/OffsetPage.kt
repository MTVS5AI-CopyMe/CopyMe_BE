package com.copymebe.copyme.core.global.pagination

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

data class OffsetMetaData(
    @Schema(
        description = "오프셋",
        required = true
    )
    val offset: Long,

    @Schema(
        description = "페이지 사이즈",
        required = true
    )
    val pageSize: Int,

    @Schema(
        description = "페이지 번호",
        required = true
    )
    val pageNumber: Int,

    @Schema(
        description = "조회된 아이템 개수",
        required = true
    )
    val itemCount: Int,

    @Schema(
        description = "전체 아이템 개수",
        required = true
    )
    val totalItemCount: Long,

    @Schema(
        description = "전체 페이지 개수",
        required = true
    )
    val totalPageCount: Int,

    @Schema(
        description = "첫 페이지 여부",
        required = true
    )
    val isFirst: Boolean,

    @Schema(
        description = "마지막 페이지 여부",
        required = true
    )
    val isLast: Boolean,
) {
    companion object {
        fun <T : Any> create(page: Page<T>): OffsetMetaData {
            val pageable = page.pageable

            return OffsetMetaData(
                offset = pageable.offset,
                pageSize = pageable.pageSize,
                pageNumber = pageable.pageNumber,
                itemCount = page.numberOfElements,
                totalItemCount = page.totalElements,
                totalPageCount = page.totalPages,
                isFirst = page.isFirst,
                isLast = page.isLast,
            )
        }
    }
}

data class OffsetPage<T>(
    @Schema(
        description = "페이지네이션 메타데이터",
        required = true,
    )
    val metaData: OffsetMetaData,

    @Schema(
        description = "데이터 목록",
        required = true
    )
    val items: T
) {
    companion object {
        fun <T : Any> create(page: Page<T>): OffsetPage<List<T>> {
            return OffsetPage(
                metaData = OffsetMetaData.create(page),
                items = page.content
            )
        }
    }
}
