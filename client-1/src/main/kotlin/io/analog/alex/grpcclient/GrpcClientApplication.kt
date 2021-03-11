package io.analog.alex.grpcclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcClientApplication

fun main() {
    runApplication<GrpcClientApplication>()
}
