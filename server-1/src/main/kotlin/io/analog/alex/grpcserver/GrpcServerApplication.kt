package io.analog.alex.grpcserver

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class GrpcServerApplication

fun main(args: Array<String>) {
	runApplication<GrpcServerApplication>(*args)
}

@RestController
class gRPC {
	private val logger = LoggerFactory.getLogger(gRPC::class.java)

	companion object {
		private const val APPLICATION_CONTENT_GRPC_VALUE = "application/x-protobuf"
	}

	@GetMapping(
		value = ["greeting"],
		consumes = [APPLICATION_CONTENT_GRPC_VALUE],
		produces = [APPLICATION_CONTENT_GRPC_VALUE]
	)
	fun greet(@RequestBody request: Greeting.GreetingRequest): Greeting.GreetingResponse {
		logger.info("Greet ${request.author} with order ${request.order}")
		return Greeting.GreetingResponse.newBuilder().setGreet(
			"We are greeting ${request.name}, with a message: ${request.message}"
		).build()
	}
}

