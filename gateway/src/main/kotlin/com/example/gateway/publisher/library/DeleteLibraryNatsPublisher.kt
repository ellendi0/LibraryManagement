package com.example.gateway.publisher.library

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.library.delete.proto.DeleteLibraryRequest
import com.example.internalapi.request.library.delete.proto.DeleteLibraryResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteLibraryNatsPublisher(
    private val connection: Connection
) : NatsPublisher<DeleteLibraryRequest, DeleteLibraryResponse> {
    override val subject = NatsSubject.Library.UPDATE
    override val parser: Parser<DeleteLibraryResponse> = DeleteLibraryResponse.parser()

    override fun request(request: DeleteLibraryRequest): Mono<DeleteLibraryResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
