package com.example.gateway.publisher

import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import reactor.core.publisher.Mono

interface NatsPublisher<RequestT : GeneratedMessageV3, ResponseT : GeneratedMessageV3> {

    val subject: String

    val parser: Parser<ResponseT>

    fun request(request: RequestT): Mono<ResponseT>
}
