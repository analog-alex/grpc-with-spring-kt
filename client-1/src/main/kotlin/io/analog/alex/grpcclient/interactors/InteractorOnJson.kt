package io.analog.alex.grpcclient.interactors

import com.fasterxml.jackson.databind.ObjectMapper
import io.analog.alex.grpcclient.models.JsonRequest
import io.analog.alex.grpcclient.models.JsonResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException

@Service
class InteractorOnJson(
    private val restTemplateJson: RestTemplate
) : Interactor<JsonRequest, JsonResponse> {
    private val logger = LoggerFactory.getLogger(InteractorOnJson::class.java)
    private val objectMapper = ObjectMapper()

    override fun interact(input: JsonRequest): JsonResponse {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val entity = HttpEntity<String>(objectMapper.writeValueAsString(input), headers)

        val response = restTemplateJson
            .postForEntity(
                "http://localhost:8080/greeting-json",
                entity,
                JsonResponse::class.java
            )
            .also { logger.debug("Response code was {}", it.statusCode) }

        return response.body ?: throw RuntimeException("Oops!")
    }
}
