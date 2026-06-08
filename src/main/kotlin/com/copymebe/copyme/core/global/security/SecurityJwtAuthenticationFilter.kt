package com.copymebe.copyme.core.global.security

import com.copymebe.copyme.core.global.exception.BaseException
import com.copymebe.copyme.core.global.http.CustomResponseEntity
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
    private val expiredSecurityTokenException = ExpiredSecurityTokenException()

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return SecurityPaths.PUBLIC_PATHS.any {
            it == request.servletPath
        }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            request.resolveToken()
                ?.takeIf(jwtTokenProvider::validateTokenOrThrow)
                ?.let(jwtTokenProvider::getAuthentication)
                ?.let { authentication ->
                    SecurityContextHolder.getContext().authentication = authentication
                }

            filterChain.doFilter(request, response)
        } catch (e: ExpiredJwtException) {
            println(e)
            // 만료된 토큰인 경우
            response.writeErrorResponse(
                objectMapper = objectMapper,
                exception = expiredSecurityTokenException
            )
        } catch (e: Exception) {
            println(e)
            response.writeErrorResponse(
                objectMapper = objectMapper,
                exception = BaseException(
                    code = "UNKNOWN",
                    status = HttpStatus.INTERNAL_SERVER_ERROR,
                    message = e.message!!
                )
            )
        }
    }
}

/**
 * 헤더에서 토큰 분리
 */
private fun HttpServletRequest.resolveToken(): String? {
    return this.getHeader("Authorization")
        ?.substringAfter("Bearer ")
}

/**
 * 에러 응답으로 rewrite
 */
private fun HttpServletResponse.writeErrorResponse(
    objectMapper: ObjectMapper,
    exception: BaseException,
) {
    // Response Metadata 설정
    this.apply {
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
        .let(this.writer::write)
}