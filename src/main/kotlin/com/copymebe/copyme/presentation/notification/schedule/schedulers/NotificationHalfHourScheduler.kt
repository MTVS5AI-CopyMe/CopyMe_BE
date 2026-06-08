package com.copymebe.copyme.presentation.notification.schedule.schedulers

import com.copymebe.copyme.core.domain.member.member.MemberRepo
import com.copymebe.copyme.core.domain.member.member.models.Member
import com.copymebe.copyme.core.domain.notification.notification.Notification
import com.copymebe.copyme.core.domain.notification.notification.NotificationRepo
import com.copymebe.copyme.core.domain.notification.notification.events.QuestNotificationCreatedExternalEvent
import com.copymebe.copyme.core.domain.notification.schedule.NotificationScheduleRepo
import com.copymebe.copyme.core.domain.quest.quest_answer.QuestAnswerRepo
import com.copymebe.copyme.core.domain.quest.quest_image.QuestImageRepo
import com.copymebe.copyme.core.domain.quest.quest_image.models.QuestImage
import com.copymebe.copyme.core.global.third_party.firebase.FirebaseMessagingService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class NotificationHalfHourScheduler(
    private val memberRepo: MemberRepo,
    private val notificationRepo: NotificationRepo,
    private val notificationScheduleRepo: NotificationScheduleRepo,
    private val questImageRepo: QuestImageRepo,
    private val questAnswerRepo: QuestAnswerRepo,
    private val firebaseMessagingService: FirebaseMessagingService,
    private val objectMapper: ObjectMapper,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 0/30 * * * *")
    @Transactional
    fun runEveryHalfHour() {
        val membersToQuestImage = findTargetMembers() // 알림 타겟 멤버 목록 찾기
            // 멤버 to 해당 멤버의 응답하지 않은 이미지 찾기
            .let(::findQuestImagesForMember)
            // 멤버 디바이스가 존재하는 데이터만 필터링
            .filterKeys { it.devices.isNotEmpty() }

        membersToQuestImage.forEach { (member, questImage) ->
            // 응답하지 않은 이미지가 없으면 다음으로
            if (questImage == null) {
                return@forEach
            }

            val fcmTokens = member.devices.map { it.fcmToken }

            val event = QuestNotificationCreatedExternalEvent(
                questId = questImage.id.toString()
            )

            val payload = objectMapper.createObjectNode()
                .put("questId", questImage.id.toString())

            val newNotification = Notification.create(
                memberId = member.id,
                title = "이미지 따라하기",
                description = "잠시 휴식을 취하며 이미지를 따라해보세요 😊",
                eventKey = event.eventId,
                payload = payload.toString()
            )

            notificationRepo.save(newNotification)

            firebaseMessagingService.sendMessages(
                fcmTokens = fcmTokens,
                title = newNotification.title,
                body = newNotification.description,
                imageUrl = questImage.imageUrl,
                eventId = newNotification.eventKey,
                payload = payload,
            )

        }
    }

    /**
     * 알림 타겟 멤버 목록 찾기
     */
    private fun findTargetMembers(): List<Member> {
        val now = LocalDateTime.now()
        val nowHm = now.toLocalTime().truncatedTo(ChronoUnit.MINUTES)
        log.info("Notification scheduler started at {}", now)

        val schedules = notificationScheduleRepo.findAllByTime(nowHm)
            .ifEmpty {
                log.info("No notification schedules matched {}", nowHm)
                return emptyList()
            }

        val memberIds = schedules.map { it.memberId }.distinct()
        val members = memberRepo.findAllById(memberIds)

        return members
    }

    /**
     * 멤버 to 해당 멤버의 응답하지 않은 이미지 찾기
     */
    private fun findQuestImagesForMember(members: List<Member>): Map<Member, QuestImage?> {
        val memberIds = members.map { it.id }

        val questImages = questImageRepo.findAll()
        val questAnswers = questAnswerRepo.findAllByMemberIdInAndDeletedAtNull(memberIds)

        val memberToQuestImage = members.associateWith { member ->
            // 멤버 응답만 필터링
            val memberAnswers = questAnswers.filter { qa -> qa.isOwner(member.id) }

            if (memberAnswers.isEmpty()) {
                return@associateWith questImages.firstOrNull()
            }

            // 멤버가 응답하지 않은 이미지들 중 첫번째 선택
            questImages.firstOrNull { image ->
                memberAnswers.all { answer -> image.id != answer.questImage.id }
            }
        }

        return memberToQuestImage
    }
}