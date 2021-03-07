package io.analog.alex.grpcclient.web

import io.analog.alex.grpcclient.configuration.ActionOrderSingleton
import io.analog.alex.grpcclient.interactors.Interactor
import io.analog.alex.grpcclient.interactors.InteractorOnGrpc
import io.analog.alex.grpcclient.interactors.InteractorOnProtobuf
import io.analog.alex.grpcserver.GreetingRequest
import io.analog.alex.grpcserver.GreetingResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HttpController(
    private val interactorOnProtobuf: InteractorOnProtobuf,
    private val interactorOnGrpc: InteractorOnGrpc
) {
    private val logger = LoggerFactory.getLogger(HttpController::class.java)

    @GetMapping("greet")
    fun greet(@RequestParam(required = true) name: String) = interact(interactorOnProtobuf, name)

    @GetMapping("greet-via-grpc")
    fun greetWithGrpc(@RequestParam(required = true) name: String) = interact(interactorOnGrpc, name)

    /*
     */
    private fun interact(
        interactor: Interactor<GreetingRequest, GreetingResponse>,
        name: String
    ): String {
        logger.info(
            "Greeting request for name {}", name
        )

        return interactor.interact(
            GreetingRequest.newBuilder()
                .setName(name)
                .setAuthor("SPRING_CLIENT_APP")
                .setMessage("I see you!")
                .setOrder(ActionOrderSingleton.order.incrementAndGet())
                .build()
        ).greet
    }
}
