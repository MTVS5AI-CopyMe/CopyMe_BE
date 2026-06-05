package com.copymebe.copyme.core.global.third_party.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.io.InputStream

@Configuration
class FirebaseConfig(
    @Value("classpath:ServiceAccountKey.json")
    private val serviceAccountKey: Resource
) {
    private val firebaseApp: FirebaseApp by lazy {
        val inputStream: InputStream = serviceAccountKey.inputStream

        val options = FirebaseOptions.builder()
            .setCredentials(
                GoogleCredentials.fromStream(inputStream)
            )
            .build()

        FirebaseApp.initializeApp(options)
    }

    @Bean
    fun initFirebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance(firebaseApp)
    }
}