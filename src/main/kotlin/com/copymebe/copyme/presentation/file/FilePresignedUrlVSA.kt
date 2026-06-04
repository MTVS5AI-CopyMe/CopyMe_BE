package com.copymebe.copyme.presentation.file

import com.copymebe.copyme.core.global.http.CustomResponseEntity
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class FilePresignedUrlRequest(
    @Schema(
        description = "파일 키 (확장자 포함)",
        example = "test.png"
    )
    @field:NotBlank(message = "파일 키는 필수 입력 항목입니다.")
    @field:Pattern(
        regexp = "^[^/\\s]+\\.[^/\\s]+$",
        message = "파일 키 형식이 올바르지 않습니다. (예: filename.ext)"
    )
    val fileKey: String,

    @Schema(
        description = "MIME 타입",
        example = "image/png"
    )
    @field:NotBlank(message = "MIME 타입은 필수 입력 항목입니다.")
    @field:Pattern(
        regexp = "^[^/\\s]+/[^/\\s]+$",
        message = "MIME 타입 형식이 올바르지 않습니다. (예: image/png)"
    )
    val contentType: String
)

data class FilePresignedUrlResponse(
    @Schema(description = "Presigned Url")
    val presignedUrl: String,

    @Schema(description = "읽기 전용 Url")
    val resourceUrl: String,
)

@Tag(name = "File")
@RestController
class FilePresignedUrlVSA {
    @Operation(summary = "Presigned Url 발급")
    @PostMapping("/api/v1/files/presigned-url")
    fun generatePresignedUrl(
        @RequestBody @Valid req: FilePresignedUrlRequest
    ): CustomResponseEntity<FilePresignedUrlResponse> {
        // TODO: PresignedUrl 발급 로직

        return CustomResponseEntity(
            data = FilePresignedUrlResponse(
                presignedUrl = "test",
                resourceUrl = "test"
            )
        )
    }

}