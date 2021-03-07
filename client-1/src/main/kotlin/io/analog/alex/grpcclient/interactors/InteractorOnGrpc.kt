package io.analog.alex.grpcclient.interactors

import io.analog.alex.grpcserver.GreetingRequest
import io.analog.alex.grpcserver.GreetingResponse
import io.analog.alex.grpcserver.GreetingServiceGrpc
import io.grpc.ManagedChannelBuilder
import org.springframework.stereotype.Service

@Service
class InteractorOnGrpc : Interactor<GreetingRequest, GreetingResponse> {
    override fun interact(input: GreetingRequest): GreetingResponse {
        val channel = ManagedChannelBuilder.forAddress("localhost", 8081)
            .usePlaintext()
            .build()

        val stub = GreetingServiceGrpc.newBlockingStub(channel)
        return stub.sayHello(input).also {
            channel.shutdown()
        }
    }
}
