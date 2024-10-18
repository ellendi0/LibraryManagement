package com.example.grpcserverstarter

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.protobuf.services.ProtoReflectionService
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class GrpcConfiguration {

    @Bean
    fun grpcServer(applicationContext: ApplicationContext): Server {
        val serverBuilder = ServerBuilder.forPort(9091)

        val grpcServiceBeans = applicationContext.getBeansWithAnnotation(GrpcService::class.java)

        for ((_, service) in grpcServiceBeans) {
            serverBuilder.addService(service as io.grpc.BindableService)
        }

        serverBuilder.addService(ProtoReflectionService.newInstance())
        val server = serverBuilder.build()
        server.start()

        Thread {
            try {
                server.awaitTermination()
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }.start()

        return server
    }
}
