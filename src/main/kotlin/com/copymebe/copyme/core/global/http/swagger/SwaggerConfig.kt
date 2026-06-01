package com.copymebe.copyme.core.global.http.swagger

import com.copymebe.copyme.core.domain.base.ExceptionMetadata
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.full.companionObjectInstance

@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
class SwaggerConfig {
    @Bean
    fun apiExceptionsCustomizer(): OperationCustomizer {
        return OperationCustomizer { operation, handlerMethod ->
            val annotation =
                handlerMethod.getMethodAnnotation(ApiExceptions::class.java)
                    ?: return@OperationCustomizer operation

            annotation.value
                .mapNotNull { exceptionClass ->
                    exceptionClass.companionObjectInstance as? ExceptionMetadata
                }
                .groupBy { it.HTTP_STATUS }
                .forEach { (status, exceptions) ->
                    val responseCode = status.value().toString()

                    val response = run {
                        // 이미 Swagger가 만든 응답이 있으면 가져오고 없으면 새로 생성
                        operation.responses[responseCode] ?: ApiResponse()
                    }.apply {
                        description = exceptions.joinToString("\n") {
                            "- ${it.DESCRIPTION}"
                        }

                        content = Content().addMediaType(
                            "application/json",
                            MediaType().schema(
                                ObjectSchema()
                                    .addProperty("code", StringSchema())
                                    .addProperty("message", StringSchema())
                                    .addProperty("data", ObjectSchema())
                            )
                        )
                    }

                    operation.responses.addApiResponse(
                        responseCode,
                        response
                    )
                }

            operation
        }
    }
}