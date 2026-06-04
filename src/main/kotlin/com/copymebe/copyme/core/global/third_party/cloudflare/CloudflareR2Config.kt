package com.copymebe.copyme.core.global.third_party.cloudflare

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

@Configuration
class CloudflareR2Config(
    @Value($$"${cloudflare.r2.endpoint}") private val endpoint: String,
    @Value($$"${cloudflare.r2.access-key}") private val accessKey: String,
    @Value($$"${cloudflare.r2.secret-key}") private val secretKey: String
) {
    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(accessKey, secretKey)

        return S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.of("auto"))
            .build()
    }

    @Bean
    fun s3Presigner(): S3Presigner = S3Presigner.builder()
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        )
        .region(Region.of("auto"))
        .build()
}