package io.analog.alex.grpcclient.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun restTemplateProtobuf(): RestTemplate {
        return RestTemplate(
            listOf(
                ProtobufHttpMessageConverter()
            )
        )
    }
}
