package io.analog.alex.grpcserver

import io.analog.alex.grpcserver.service.GreetingService
import io.grpc.Server
import io.grpc.ServerBuilder

class GrpcServer {
    fun start(port: Int) {
        val server = ServerBuilder.forPort(port).addService(GreetingService()).build()
        server.start()
        server.awaitTermination()
    }
}

fun main() {
    GrpcServer().start(8081)
}
