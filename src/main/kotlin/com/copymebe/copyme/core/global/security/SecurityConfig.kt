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
}