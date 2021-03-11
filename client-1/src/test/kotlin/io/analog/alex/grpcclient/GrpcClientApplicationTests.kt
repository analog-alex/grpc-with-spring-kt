package io.analog.alex.grpcclient

import io.analog.alex.grpcclient.web.HttpController
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

@SpringBootTest
class GrpcClientApplicationTests {

    @Autowired
    private lateinit var httpController: HttpController

    @Test
    fun `check if response is correct for each endpoint`() {
        val protobuf = httpController.greetWithProtobuf("Test")
        val json = httpController.greetWithJson("Test")
        val grpc = httpController.greetWithGrpc("Test")

        assertEquals("We are greeting Test, with a message: I see you are using Protocol Buffers!", protobuf)
        assertEquals("We are greeting Test, with a message: I see you are using JSON!", json)
        assertEquals("We are greeting Test, with a message: I see you are using gRPC!", grpc)
    }

    @Test
    @Disabled
    fun `load test`() {
        val firstRun = runUnderLoad(20000) {
            httpController.greetWithProtobuf(UUID.randomUUID().toString())
        }

        val secondRun = runUnderLoad(20000) {
            httpController.greetWithGrpc(UUID.randomUUID().toString())
        }

        val thirdRun = runUnderLoad(20000) {
            httpController.greetWithJson(UUID.randomUUID().toString())
        }

        prettyPrint("Protobuf result", firstRun)
        prettyPrint("gRPC result", secondRun)
        prettyPrint("Json result", thirdRun)
    }

    private fun runUnderLoad(times: Int, task: () -> Unit): List<Long> {
        val context = Executors.newFixedThreadPool(25).asCoroutineDispatcher()

        return runBlocking(context) {
            (1..times).map { _ ->
                async {
                    measureTimeMillis {
                        task.invoke()
                    }
                }.await()
            }
        }
    }

    private fun prettyPrint(title: String, values: List<Long>) {
        println(
            """
                ======================================
                $title
                -----------
                Average: ${values.average()}
                Minimum: ${values.minOrNull()}
                Maximum: ${values.maxOrNull()}
                +++++++++++++++++++++++++++++++++++
            """.trimIndent()
        )
    }
}
