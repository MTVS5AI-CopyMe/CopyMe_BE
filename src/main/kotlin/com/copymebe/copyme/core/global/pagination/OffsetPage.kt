package com.copymebe.copyme.core.global.pagination

import org.springframework.data.domain.Page

data class OffsetMetaData(
    /**
     * 오프셋
     */
    val offset: Long,
    /**
     * 페이지 사이즈
     */
    val pageSize: Int,
    /**
     * 페이지 번호
     */
    val pageNumber: Int,
    /**
     * 조회된 아이템 개수
     */
    val itemCount: Int,
    /**
     * 전체 아이템 개수
     */
    val totalItemCount: Long,
    /**
     * 전체 페이지 개수
     */
    val totalPageCount: Int,
    /**
     * 첫 페이지 여부
     */
    val isFirst: Boolean,
    /**
     * 마지막 페이지 여부
     */
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
    val metaData: OffsetMetaData,
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
