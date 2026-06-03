package com.copymebe.copyme.core.global.security

import org.springframework.security.core.Authentication
import java.util.*

fun Authentication.getUserId(): UUID {
    return UUID.fromString(this.principal as String)
}