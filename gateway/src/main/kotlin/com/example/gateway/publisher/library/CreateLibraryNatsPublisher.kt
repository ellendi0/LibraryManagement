package com.example.gateway.publisher.library

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.library.create.proto.CreateLibraryRequest
import com.example.internalapi.request.library.create.proto.CreateLibraryResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateLibraryNatsPublisher(
    private val connection: Connection
) : NatsPublisher<CreateLibraryRequest, CreateLibraryResponse> {
    override val subject = NatsSubject.Library.CREATE
    override val parser: Parser<CreateLibraryResponse> = CreateLibraryResponse.parser()

    override fun request(request: CreateLibraryRequest): Mono<CreateLibraryResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
