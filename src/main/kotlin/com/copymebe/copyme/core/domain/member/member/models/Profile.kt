package com.copymebe.copyme.core.domain.member.member.models

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Profile protected constructor(
    @Column(name = "profile_image_url")
    var profileImageUrl: String,

    @Column(name = "profile_nickname")
    var nickname: String,
) {
    companion object {
        fun create(
            profileImageUrl: String,
            nickname: String,
        ): Profile {
            return Profile(
                profileImageUrl = profileImageUrl,
                nickname = nickname,
            )
        }
    }

}