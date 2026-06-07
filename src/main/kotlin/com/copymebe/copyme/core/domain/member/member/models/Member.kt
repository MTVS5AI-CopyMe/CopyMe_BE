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
        const val PASSWORD_REGEX = "((?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,20})"

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
    fun upsertDevice(
        deviceUid: String,
        fcmToken: String,
        refreshToken: String,
    ) {
        devices.find { it.deviceUid == deviceUid }
            ?.apply {
                this.fcmToken = fcmToken
                this.refreshToken = refreshToken
            }
            ?: run {
                devices.add(
                    MemberDeviceInfo.create(
                        member = this,
                        deviceUid = deviceUid,
                        fcmToken = fcmToken,
                        refreshToken = refreshToken,
                    )
                )
            }
    }

    fun removeDevice(deviceUid: String) {
        devices.removeIf { it.deviceUid == deviceUid }
    }

    fun updateProfile(
        profileImageUrl: String,
        nickname: String,
    ) {
        profile.profileImageUrl = profileImageUrl
        profile.nickname = nickname
    }
}
