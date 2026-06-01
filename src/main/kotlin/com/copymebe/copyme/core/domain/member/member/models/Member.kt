package com.copymebe.copyme.core.domain.member.member.models

import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member protected constructor(
    @Column(name = "email", nullable = false, unique = true)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Embedded
    var profile: Profile,

    @OneToMany(
        mappedBy = "member",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val devices: MutableList<MemberDeviceInfo> = mutableListOf(),
) : BaseEntity() {
    companion object {
        fun create(
            email: String,
            password: String,
            profileImageUrl: String,
            nickname: String
        ): Member {
            return Member(
                email = email,
                password = password,
                profile = Profile.create(
                    profileImageUrl = profileImageUrl,
                    nickname = nickname,
                ),
            )
        }
    }

    // Device
    fun addDevice(
        deviceUid: String,
        fcmToken: String,
        refreshToken: String,
    ) {
        devices.add(
            MemberDeviceInfo.create(
                member = this,
                deviceUid = deviceUid,
                fcmToken = fcmToken,
                refreshToken = refreshToken,
            )
        )
    }

    fun removeDevice(deviceUid: String) {
        devices.removeIf { it.deviceUid == deviceUid }
    }
}