package com.copymebe.copyme.core.global.pagination

data class CursorMetaData(
    /**
     * 다음 커서 ID
     */
    val nextCursorId: String? = null,
    /**
     * 마지막 페이지 여부
     */
    val isEnd: Boolean = false,
)

data class CursorPage<T>(
    val metaData: CursorMetaData,
    val items: T,
)
