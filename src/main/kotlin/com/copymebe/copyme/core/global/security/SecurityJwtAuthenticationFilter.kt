package com.copymebe.copyme.core.global.security

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import com.copymebe.copyme.core.global.exception.BaseException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import tools.jackson.databind.ObjectMapper

/**
 * JWT 인증 필터
 */
@Component
class SecurityJwtAuthenticationFilter(
    private val jwtTokenProvider: SecurityJwtTokenProvider,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {
    private val defaultAuthException = BaseException(
        code = "UNAUTHORIZED",
        status = HttpStatus.UNAUTHORIZED,
        message = "UNAUTHORIZED"
    )

    private val expiredAccessTokenException = ExpiredAccessTokenException()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            resolveToken(request)
                ?.takeIf(jwtTokenProvider::validateTokenOrThrow)
                ?.let(jwtTokenProvider::getAuthentication)
                ?.let { authentication ->
                    SecurityContextHolder.getContext().authentication = authentication
                }

            filterChain.doFilter(request, response)
        } catch (_: ExpiredJwtException) {
            // 만료된 토큰인 경우
            sendErrorResponse(
                response = response,
                exception = expiredAccessTokenException
            )
        } catch (_: Exception) {
            sendErrorResponse(
                response = response,
                exception = defaultAuthException
            )
        }
    }

    /**
     * 헤더에서 토큰 분리
     */
    private fun resolveToken(request: HttpServletRequest): String? {
        return request
            .getHeader("Authorization")
            ?.substringAfter("Bearer ")
    }

    /**
     * 에러 응답으로 rewrite
     */
    private fun sendErrorResponse(
        response: HttpServletResponse,
        exception: BaseException,
    ) {
        // Response Metadata 설정
        response.apply {
            status = exception.status.value()
            contentType = MediaType.APPLICATION_JSON_VALUE
            characterEncoding = "UTF-8"
        }

        // Response Body 설정
        CustomResponseEntity(
            code = exception.code,
            message = exception.message,
            data = null
        ).let(objectMapper::writeValueAsString)
            .let(response.writer::write)
    }
}