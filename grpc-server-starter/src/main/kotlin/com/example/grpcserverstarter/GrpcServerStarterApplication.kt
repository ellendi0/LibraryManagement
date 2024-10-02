package com.example.grpcserverstarter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcServerStarterApplication

fun main(args: Array<String>) {
    runApplication<GrpcServerStarterApplication>(*args)
}
