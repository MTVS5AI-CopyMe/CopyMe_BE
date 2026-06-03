package com.copymebe.copyme.core.global.pagination

import io.swagger.v3.oas.annotations.media.Schema

data class CursorMetaData(
    @Schema(description = "다음 커서 ID")
    val nextCursorId: String? = null,

    @Schema(description = "마지막 페이지 여부")
    val isEnd: Boolean = false,
)

data class CursorPage<T>(
    @Schema(description = "페이지네이션 메타데이터")
    val metaData: CursorMetaData,

    @Schema(description = "데이터 목록")
    val items: T,
)
