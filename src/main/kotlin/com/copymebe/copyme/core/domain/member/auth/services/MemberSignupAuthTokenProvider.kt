package com.copymebe.copyme.core.domain.member.auth.services

import com.copymebe.copyme.core.domain.member.auth.MemberSignupEmailTokenInvalidException
import com.copymebe.copyme.core.domain.member.auth.models.MemberSignupAuthenticationManagerRequest
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey

/**
 * 회원가입 인증 토큰 생성
 */
@Component
class MemberSignupAuthTokenProvider(
    @Value($$"${spring.member.signup.auth-token.secret}")
    private val secretKeyStr: String,
) {
    private val secretKey: SecretKey by lazy {
        secretKeyStr
            .toByteArray()
            .let(Keys::hmacShaKeyFor)
    }

    private val expiredDuration = Duration.ofMinutes(MemberSignupAuthenticationManagerRequest.EXPIRED_MINUTES)

    fun createToken(email: String): String {
        val expiredAt = Date(System.currentTimeMillis() + expiredDuration.toMillis())

        return Jwts.builder()
            .subject("Signup")
            .claim("email", email)
            .signWith(secretKey)
            .expiration(expiredAt)
            .compact()
    }

    fun parseEmailOrThrow(token: String): String {
        println(token)
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .let {
                    it.payload["email"] as String
                }
        } catch (e: Exception) {
            throw MemberSignupEmailTokenInvalidException()
        }
    }
}