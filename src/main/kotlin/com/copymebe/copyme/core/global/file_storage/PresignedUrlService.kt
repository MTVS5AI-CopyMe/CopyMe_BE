package com.copymebe.copyme.core.global.file_storage

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Service
class PresignedUrlService(
    private val s3Presigner: S3Presigner,
    @Value($$"${cloudflare.r2.bucket-name}") private val bucketName: String,
    @Value($$"${cloudflare.r2.cdn}") private val cdn: String
) {
    /**
     * 10분간 유효한 업로드 URL
     */
    fun createPresignedUrl(
        fileKey: String,
    ): Pair<String, String> {
        val newFileKey = run {
            val path = "members/quest-answers/"
            val nanoId = NanoIdUtils.randomNanoId()
            "${path}${nanoId}-${fileKey}"
        }

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(newFileKey)
            .build()

        val putObjectPresignedRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedUrl = s3Presigner.presignPutObject(putObjectPresignedRequest).url()
        val resourceUrl = "${cdn}${presignedUrl.path}"

        return Pair(
            presignedUrl.toString(),
            resourceUrl
        )
    }
}