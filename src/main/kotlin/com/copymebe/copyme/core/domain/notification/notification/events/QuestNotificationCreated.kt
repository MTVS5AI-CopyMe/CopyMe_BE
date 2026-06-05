package com.copymebe.copyme.core.domain.notification.notification.events

class ExternalEvent

/**
 * [외부 이벤트]
 * 퀘스트 알림 생성 이벤트
 */
data class QuestNotificationCreatedExternalEvent(
    val eventKey: String = EVENT_KEY,
    val questId: String,
) {
    companion object {
        const val EVENT_KEY = "EXTERNAL__QUEST_NOTIFICATION__CREATED"
    }
}