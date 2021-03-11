package io.analog.alex.grpcclient.web

import io.analog.alex.grpcclient.configuration.ActionOrderSingleton
import io.analog.alex.grpcclient.interactors.Interactor
import io.analog.alex.grpcclient.interactors.InteractorOnGrpc
import io.analog.alex.grpcclient.interactors.InteractorOnJson
import io.analog.alex.grpcclient.interactors.InteractorOnProtobuf
import io.analog.alex.grpcclient.models.JsonRequest
import io.analog.alex.grpcserver.GreetingRequest
import io.analog.alex.telephony.Call
import io.analog.alex.telephony.Status
import org.apache.tomcat.util.security.MD5Encoder
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HttpController(
    private val interactorOnJson: InteractorOnJson,
    private val interactorOnProtobuf: InteractorOnProtobuf,
    private val interactorOnGrpc: InteractorOnGrpc
) {
    private val logger = LoggerFactory.getLogger(HttpController::class.java)

    @GetMapping("greet-via-protobuf")
    fun greetWithProtobuf(@RequestParam(required = true) name: String): String = interact(interactorOnProtobuf) {
        GreetingRequest.newBuilder()
            .setName(name)
            .setAuthor("SPRING_CLIENT_APP")
            .setMessage("I see you are using Protocol Buffers!")
            .setOrder(ActionOrderSingleton.order.incrementAndGet())
            .build()
    }.greet

    @GetMapping("greet-via-grpc")
    fun greetWithGrpc(@RequestParam(required = true) name: String): String = interact(interactorOnGrpc) {
        GreetingRequest.newBuilder()
            .setName(name)
            .setAuthor("SPRING_CLIENT_APP")
            .setMessage("I see you are using gRPC!")
            .setOrder(ActionOrderSingleton.order.incrementAndGet())
            .build()
    }.greet

    @GetMapping("greet-via-json")
    fun greetWithJson(@RequestParam(required = true) name: String): String = interact(interactorOnJson) {
        JsonRequest(
            name = name,
            author = "SPRING_CLIENT_APP",
            message = "I see you are using JSON!",
            order = ActionOrderSingleton.order.incrementAndGet()
        )
    }.greet

    /*
     */
    private fun <Q, A> interact(
        interactor: Interactor<Q, A>,
        request: () -> Q
    ): A {
        logger.debug(
            "Greeting request {}", interactor::class.java.simpleName
        )
        return interactor.interact(request.invoke())
    }


    // -----------------


    @GetMapping("greets-via-grpc-stream")
    fun greetsStream(@RequestParam(required = true) name: String): String = interactorOnGrpc.interactStream(
        GreetingRequest.newBuilder()
            .setName(name)
            .setAuthor("SPRING_CLIENT_APP")
            .setMessage("I see you are using gRPC streams!")
            .setOrder(ActionOrderSingleton.order.incrementAndGet())
            .build()
    )
        .let { "We are now listening to the gRPC server stream!" }

    @GetMapping("telephony-via-grpc-bi-stream")
    fun telephonyStream(@RequestParam(required = true) address: String): String = interactorOnGrpc.interactBiDirectionalStream(
        Call.newBuilder()
            .setId(address.hashCode().toString())
            .setAddress(address)
            .setStatus(Status.NONE)
            .setOrder(ActionOrderSingleton.order.incrementAndGet())
            .build()
    )
        .let { "We are now listening to the gRPC server bi directional stream!" }
}
