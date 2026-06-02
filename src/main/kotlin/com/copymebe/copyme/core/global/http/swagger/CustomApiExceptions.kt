package com.copymebe.copyme.core.global.http.swagger

import com.copymebe.copyme.core.global.exception.BaseException
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomApiExceptions(
    vararg val value: KClass<out BaseException>
)