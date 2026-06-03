package com.copymebe.copyme.core.global.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import java.util.*

fun Authentication.getUserId(): UUID {
    val user = this.principal as User

    return UUID.fromString(user.username)
}