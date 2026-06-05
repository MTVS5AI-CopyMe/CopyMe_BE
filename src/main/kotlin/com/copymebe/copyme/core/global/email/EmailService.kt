package com.copymebe.copyme.core.global.email

import jakarta.mail.internet.InternetAddress
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender,

    @Value($$"${spring.mail.custom-props.sender.display-name}")
    private val senderDisplayName: String,

    @Value($$"${spring.mail.custom-props.sender.address}")
    private val senderAddress: String,
) {
    fun sendSimpleMail(
        /**
         * 수신자 이메일 주소 목록
         */
        to: List<String>,
        /**
         * 제목
         */
        subject: String,
        /**
         * 내용
         */
        text: String
    ) {
        try {
            val message = mailSender.createMimeMessage()
                .also { message ->
                    MimeMessageHelper(
                        message,
                        true,
                        "UTF-8"
                    ).apply {
                        setFrom(
                            InternetAddress(
                                senderAddress,
                                senderDisplayName,
                                "UTF-8"
                            )
                        )

                        setTo(to.toTypedArray())
                        setSubject(subject)
                        setText(text)
                    }
                }

            mailSender.send(message)
        } catch (e: Exception) {
            println(e)
            throw RuntimeException(e)
        }
    }
}