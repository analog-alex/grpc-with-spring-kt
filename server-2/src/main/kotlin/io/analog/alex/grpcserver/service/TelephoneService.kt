package io.analog.alex.grpcserver.service

import io.analog.alex.telephony.Action
import io.analog.alex.telephony.Call
import io.analog.alex.telephony.Status
import io.analog.alex.telephony.TelephonyServiceGrpc
import io.grpc.stub.StreamObserver

class TelephoneService: TelephonyServiceGrpc.TelephonyServiceImplBase() {

    private val map = mutableMapOf<String, Int>()

    override fun call(responseObserver: StreamObserver<Action>): StreamObserver<Call> {

        return object: StreamObserver<Call> {
            override fun onNext(value: Call) {
                val times = map.getOrPut(value.address, { 1 })
                map[value.address] = times + 1
                return when (times) {
                    1 -> responseObserver.onNext(
                        Action.newBuilder().setName("Picking up").setStatus(Status.PICKED_UP).build().also { println(it) }
                    )
                    2 -> responseObserver.onNext(
                        Action.newBuilder().setName("Still going...").setStatus(Status.ON_GOING).build().also { println(it) }
                    )
                    3 -> responseObserver.onNext(
                        Action.newBuilder().setName("Termination").setStatus(Status.TERMINATED).build().also { println(it) }
                    )
                    4 -> {
                        responseObserver.onCompleted()
                        this.onCompleted()
                    }
                    else -> responseObserver.onError(Exception("Oops!"))
                }
            }

            override fun onError(t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onCompleted() {
                println("Call completed on Server side!")
            }
        }
    }
}