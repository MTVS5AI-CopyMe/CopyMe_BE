package com.copymebe.copyme.core.global.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey

/**
 * Security 전용 JWT 토큰 생성/검증
 */
@Component
class SecurityJwtTokenProvider(
    @Value($$"${spring.security.jwt.secret}")
    private val secretKeyStr: String,
    @Value($$"${spring.security.jwt.access-token-duration}")
    private val accessTokenDuration: Duration,
    @Value($$"${spring.security.jwt.refresh-token-duration}")
    private val refreshTokenDuration: Duration,
) {
    private val secretKey: SecretKey by lazy {
        secretKeyStr
            .toByteArray()
            .let(Keys::hmacShaKeyFor)
    }

    fun createAccessToken(
        authentication: Authentication,
        userId: String,
    ): String {
        val expiredAt = Date(
            System.currentTimeMillis() + accessTokenDuration.toMillis()
        )

        return Jwts.builder()
            .subject(authentication.name)
            .claim("userId", userId)
            .claim("roles", authentication.authorities.map { it.authority })
            .signWith(secretKey)
            .expiration(expiredAt)
            .compact()
    }

    fun createRefreshToken(
        authentication: Authentication,
        userId: String,
    ): String {
        val expiredAt = Date(
            System.currentTimeMillis() + refreshTokenDuration.toMillis()
        )

        return Jwts.builder()
            .subject(authentication.name)
            .claim("userId", userId)
            .claim("roles", authentication.authorities.map { it.authority })
            .signWith(secretKey)
            .expiration(expiredAt)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val (userId, authorities) = run {
            Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        }.let { claims ->
            val userId = claims["userId"].toString()
            val roles = claims["roles"] as? List<*> ?: emptyList<String>()
            val authorities = roles.map { role -> SimpleGrantedAuthority(role.toString()) }

            Pair(userId, authorities)
        }

        return UsernamePasswordAuthenticationToken(
            User(userId, "", authorities),
            token,
            authorities
        )

    }

    fun validateTokenOrThrow(
        token: String,
    ): Boolean {
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)

        return true
    }
}