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

object SwaggerSecurityConst {
    const val BEARER_AUTH = "Bearer Authentication"
}

@Configuration
@SecurityScheme(
    name = SwaggerSecurityConst.BEARER_AUTH,
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
class SwaggerConfig {
    @Bean
    fun apiExceptionsCustomizer(): OperationCustomizer {
        return OperationCustomizer { operation, handlerMethod ->
            val annotation =
                handlerMethod.getMethodAnnotation(CustomApiExceptions::class.java)
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
                        // Description에 에러 코드들 명시
                        description = exceptions.joinToString("\n") {
                            "- ${it.DESCRIPTION}"
                        }

                        // Content에 공통 에러 바디 명시
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