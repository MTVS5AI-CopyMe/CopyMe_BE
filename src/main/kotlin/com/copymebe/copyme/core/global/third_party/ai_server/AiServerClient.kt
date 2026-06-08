package com.copymebe.copyme.core.global.third_party.ai_server

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

data class AiServerScoringRequest(
    val mode: String,
    val originImageUrl: String,
    val userImageUrl: String,

    var apiKey: String = ""
)

data class AiServerScoringResponse(
    val score: Double
)

@Component
class AiServerClient(
    restClientBuilder: RestClient.Builder,

    @Value($$"${ai-server.url}")
    private val url: String,

    @Value($$"${ai-server.secret}")
    private val secretKey: String,
) {
    private val restClient: RestClient = restClientBuilder
        .baseUrl(url)
        .build()

    fun scoring(request: AiServerScoringRequest): AiServerScoringResponse {
        request.apiKey = secretKey

        println(request.apiKey)

        return restClient.post()
            .uri("/score")
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body<AiServerScoringResponse>()!!
    }
}