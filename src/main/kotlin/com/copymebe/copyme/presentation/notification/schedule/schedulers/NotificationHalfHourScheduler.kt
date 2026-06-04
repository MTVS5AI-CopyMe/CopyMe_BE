package com.copymebe.copyme.presentation.notification.schedule.schedulers

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class NotificationHalfHourScheduler {
    // 💡 매시 0분, 30분 정각에 실행하는 크론 표현식
    @Scheduled(cron = "0 0/30 * * * *")
    fun runEveryHalfHour() {
        println("스케줄러 실행 시간: ${LocalDateTime.now()}")
    }
}