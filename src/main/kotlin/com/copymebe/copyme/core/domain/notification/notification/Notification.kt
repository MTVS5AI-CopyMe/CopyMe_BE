package com.copymebe.copyme.core.domain.notification.notification

import com.copymebe.copyme.core.domain.base.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "notification")
class Notification protected constructor(
    @Column(name = "member_id", nullable = false)
    val memberId: UUID,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "event_key", nullable = false)
    var eventKey: String,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false)
    var payload: String,

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false,
) : BaseEntity() {
    companion object {
        fun create(
            memberId: UUID,
            title: String,
            description: String,
            eventKey: String,
            payload: String,
        ): Notification {
            return Notification(
                memberId = memberId,
                title = title,
                description = description,
                eventKey = eventKey,
                payload = payload,
            )
        }
    }

    fun isOwner(memberId: UUID): Boolean {
        return this.memberId == memberId
    }

    fun read() {
        this.isRead = true
    }
}
