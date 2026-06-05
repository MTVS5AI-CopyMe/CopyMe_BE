package com.copymebe.copyme.core.global.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val securityJwtAuthenticationFilter: SecurityJwtAuthenticationFilter,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationManager {
        return DaoAuthenticationProvider(userDetailsService)
            .apply { setPasswordEncoder(passwordEncoder) }
            .let { ProviderManager(it) }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.apply {
            csrf { it.disable() }
            headers {
                //  H2 콘솔 iframe 허용을 위한 설정
                it.frameOptions { frameOptions -> frameOptions.sameOrigin() }
            }
            sessionManagement {
                // 세션 사용 안 함 -> JWT로 대체
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            authorizeHttpRequests {
                it
                    .requestMatchers(*SecurityPaths.PUBLIC_PATHS).permitAll()
                    .anyRequest().authenticated()
            }
            addFilterBefore(
                securityJwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
        }.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            // 모든 오리진(도메인) 허용
            allowedOriginPatterns = listOf("*")

            // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE, PATCH, OPTIONS 등 전체)
            allowedMethods = listOf("*")

            // 모든 요청 헤더 허용
            allowedHeaders = listOf("*")

            // 모든 응답 헤더를 프론트엔드가 읽을 수 있도록 노출
            exposedHeaders = listOf("*")

            // 쿠키 및 인증 헤더(Bearer 토큰 등)를 동반한 요청 허용
            // allowedOriginPatterns("*")를 사용했기 때문에, true로 설정해도 에러가 나지 않음
            allowCredentials = true

            maxAge = 3600L
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }
}