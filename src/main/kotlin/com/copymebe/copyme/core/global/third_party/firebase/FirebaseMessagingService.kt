package com.copymebe.copyme.core.global.third_party.firebase

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service
import tools.jackson.databind.node.ObjectNode


@Service
class FirebaseMessagingService(
    private val firebaseMessaging: FirebaseMessaging
) {
    fun sendMessages(
        /** FCM 토큰 리스트 */
        fcmTokens: List<String>,
        /** 알림 제목 */
        title: String,
        /** 알림 내용 */
        body: String,
        /** 이미지 URL */
        imageUrl: String? = null,
        /** Data로 전달될 이벤트 키 */
        eventId: String,
        /** Data로 전달될 페이로드 */
        payload: ObjectNode
    ) {
        // 메세지 생성
        val multicastMessage = MulticastMessage.builder()
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .apply {
                        imageUrl?.let { setImage(imageUrl) }
                    }
                    .build()
            )
            .putData("eventId", eventId)
            .putData("payload", payload.toString())
            .addAllTokens(fcmTokens)
            .build()

        // 메세지 발송
        val response = FirebaseMessaging.getInstance()
            .sendEachForMulticast(multicastMessage)

        // 발송 실패한 메세지 있는 경우
        if (response.failureCount > 0) {
            val responses = response.responses

            val failedTokens = responses.indices
                // 실패한 응답만 걸러내기
                .filter { index -> responses[index]?.isSuccessful?.not() ?: false }
                .map { index -> fcmTokens[index] }

            println("List of tokens that caused failures: $failedTokens")
        }

    }
}