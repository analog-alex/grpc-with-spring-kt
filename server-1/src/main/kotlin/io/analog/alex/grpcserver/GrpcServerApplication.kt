package io.analog.alex.grpcserver

import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class GrpcServerApplication

fun main() {
    // runApplication<GrpcServerApplication>()
    GrpcServer().start(8080)
}
