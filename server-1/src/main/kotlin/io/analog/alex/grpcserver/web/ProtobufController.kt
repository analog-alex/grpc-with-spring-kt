package io.analog.alex.grpcserver.web

import io.analog.alex.grpcserver.GreetingRequest
import io.analog.alex.grpcserver.GreetingResponse
import io.analog.alex.grpcserver.configuration.MediaTypeExtended
import org.slf4j.LoggerFactory
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
}
