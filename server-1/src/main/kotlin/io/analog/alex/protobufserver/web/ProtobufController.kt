package io.analog.alex.protobufserver.web

import io.analog.alex.grpcserver.GreetingRequest
import io.analog.alex.grpcserver.GreetingResponse
import io.analog.alex.protobufserver.configuration.MediaTypeExtended
import io.analog.alex.protobufserver.models.JsonRequest
import io.analog.alex.protobufserver.models.JsonResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProtobufController {
    private val logger = LoggerFactory.getLogger(ProtobufController::class.java)

    /**
     * direct protobuf endpoint
     */
    @PostMapping(
        value = ["greeting"],
        consumes = [MediaTypeExtended.APPLICATION_PROTOBUF_VALUE],
        produces = [MediaTypeExtended.APPLICATION_PROTOBUF_VALUE]
    )
    fun greet(@RequestBody request: GreetingRequest): GreetingResponse {
        logger.info(
            "Greet {} with order {}", request.author, request.order
        )

        return GreetingResponse
            .newBuilder()
            .setGreet("We are greeting ${request.name}, with a message: ${request.message}")
            .build()
    }

    /**
     * json alternative endpoint
     */
    @PostMapping(
        value = ["greeting-json"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun greetJson(@RequestBody request: JsonRequest): JsonResponse {
        logger.info(
            "Greet {} with order {}", request.author, request.order
        )

        return JsonResponse(
            greet = "We are greeting ${request.name}, with a message: ${request.message}"
        )
    }
}
