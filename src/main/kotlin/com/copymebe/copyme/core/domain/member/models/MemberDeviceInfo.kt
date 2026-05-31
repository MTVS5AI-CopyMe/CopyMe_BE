package com.copymebe.copyme.core.domain.member.models

import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "member_device_info")
class MemberDeviceInfo protected constructor(
    @Column(name = "device_uid", nullable = false)
    var deviceUid: String,

    @Column(name = "fcm_token", nullable = false)
    var fcmToken: String,

    @Column(name = "refresh_token", nullable = false)
    var refreshToken: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
) : BaseEntity() {
    companion object {
        fun create(
            member: Member,
            deviceUid: String,
            fcmToken: String,
            refreshToken: String
        ): MemberDeviceInfo {
            return MemberDeviceInfo(
                deviceUid = deviceUid,
                fcmToken = fcmToken,
                refreshToken = refreshToken,
                member = member,
            )
        }
    }
}