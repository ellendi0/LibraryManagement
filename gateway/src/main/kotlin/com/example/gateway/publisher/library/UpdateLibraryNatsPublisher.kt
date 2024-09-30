package com.example.gateway.publisher.library

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.library.update.proto.UpdateLibraryRequest
import com.example.internalapi.request.library.update.proto.UpdateLibraryResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateLibraryNatsPublisher(
    private val connection: Connection
) : NatsPublisher<UpdateLibraryRequest, UpdateLibraryResponse> {
    override val subject = NatsSubject.Library.UPDATE
    override val parser: Parser<UpdateLibraryResponse> = UpdateLibraryResponse.parser()

    override fun request(request: UpdateLibraryRequest): Mono<UpdateLibraryResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
