package com.copymebe.copyme.core.domain.base

import com.github.f4b6a3.uuid.UuidCreator
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@SQLRestriction("deleted_at IS NULL")
abstract class BaseEntity {
    @Id
    val id: UUID = UuidCreator.getTimeOrderedEpoch()

    @CreatedDate
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedDate
    @Column(
        name = "updated_at",
        nullable = false
    )
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    @Column(
        name = "deleted_at",
        nullable = true
    )
    var deletedAt: LocalDateTime? = null
        protected set

    fun softDelete() {
        this.deletedAt = LocalDateTime.now()
    }
}