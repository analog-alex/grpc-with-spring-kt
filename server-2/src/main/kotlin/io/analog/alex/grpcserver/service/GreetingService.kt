package io.analog.alex.grpcserver.service

import io.analog.alex.grpcserver.GreetingRequest
import io.analog.alex.grpcserver.GreetingResponse
import io.analog.alex.grpcserver.GreetingServiceGrpc
import io.grpc.stub.StreamObserver

class GreetingService : GreetingServiceGrpc.GreetingServiceImplBase() {

    override fun sayHello(request: GreetingRequest, responseObserver: StreamObserver<GreetingResponse>) {

        val response = GreetingResponse
            .newBuilder()
            .setGreet("We are greeting ${request.name}, with a message: ${request.message}")
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    override fun sayHellos(request: GreetingRequest, responseObserver: StreamObserver<GreetingResponse>) {

        (1..100).forEach { index ->
            responseObserver.onNext(
                GreetingResponse
                    .newBuilder()
                    .setGreet("We are greeting ${request.name}, with a message: ${request.message}, Index $index")
                    .build()
            )
            Thread.sleep(500)
        }

        responseObserver.onCompleted()
    }
}
