package io.analog.alex.grpcclient.interactors

import io.analog.alex.grpcserver.GreetingRequest
import io.analog.alex.grpcserver.GreetingResponse
import io.analog.alex.grpcserver.GreetingServiceGrpc
import io.analog.alex.telephony.Action
import io.analog.alex.telephony.Call
import io.analog.alex.telephony.Status
import io.analog.alex.telephony.TelephonyServiceGrpc
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.acos

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

    /**
     * extras
     */
    fun interactStream(input: GreetingRequest) {
        val channel = ManagedChannelBuilder.forAddress("localhost", 8081)
            .usePlaintext()
            .build()

        val stub = GreetingServiceGrpc.newStub(channel)
        val observer = object: StreamObserver<GreetingResponse> {
            override fun onNext(value: GreetingResponse) {
                // do something interesting HERE
                println(value)
            }

            override fun onError(t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onCompleted() {
                println("All done!")
                channel.shutdown()
            }
        }

        stub.sayHellos(input, observer)
    }

    fun interactBiDirectionalStream(input: Call) {
        val channel = ManagedChannelBuilder.forAddress("localhost", 8081)
            .usePlaintext()
            .build()

        val stub = TelephonyServiceGrpc.newStub(channel)
        val responseObserver = AtomicReference<StreamObserver<Call>>()

        val observer = object: StreamObserver<Action> {
            override fun onNext(value: Action) {
                println(value)
                responseObserver.get().onNext(input)
            }

            override fun onError(t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onCompleted() {
                println("All done!")
                channel.shutdown()
            }
        }

        val response = stub.call(observer)
        response.onNext(input)
        responseObserver.set(response)
    }
}
