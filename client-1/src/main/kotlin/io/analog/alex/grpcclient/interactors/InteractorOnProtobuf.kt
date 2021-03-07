package io.analog.alex.grpcclient.interactors

import io.analog.alex.grpcserver.GreetingRequest
import io.analog.alex.grpcserver.GreetingResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException

@Service
class InteractorOnProtobuf(
    private val restTemplateProtobuf: RestTemplate
) : Interactor<GreetingRequest, GreetingResponse> {
    private val logger = LoggerFactory.getLogger(InteractorOnProtobuf::class.java)

    override fun interact(input: GreetingRequest): GreetingResponse {
        val response = restTemplateProtobuf
            .postForEntity("http://localhost:8080/greeting", input, GreetingResponse::class.java)
            .also { logger.info("Response code was {}", it.statusCode) }

        return response.body ?: throw RuntimeException("Oops!")
    }
}
