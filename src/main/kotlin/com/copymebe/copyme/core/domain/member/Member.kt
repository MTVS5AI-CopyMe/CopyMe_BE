package com.copymebe.copyme.core.domain.member

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import com.copymebe.copyme.core.domain.base.BaseEntity

@Entity
@Table(name = "member")
class Member protected constructor(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var age: Int,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, unique = true, length = 13)
    var phone: String
) : BaseEntity() {
    companion object {
        fun create(
            name: String,
            age: Int,
            email: String,
            password: String,
            phone: String,
        ): Member {
            return Member(
                name = name,
                age = age,
                email = email,
                password = password,
                phone = phone,
            )
        }
    }
}