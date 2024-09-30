package com.example.gateway.publisher.author

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.author.update.proto.UpdateAuthorRequest
import com.example.internalapi.request.author.update.proto.UpdateAuthorResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UpdateAuthorNatsPublisher(
    private val connection: Connection
) : NatsPublisher<UpdateAuthorRequest, UpdateAuthorResponse> {
    override val subject = NatsSubject.Author.UPDATE
    override val parser: Parser<UpdateAuthorResponse> = UpdateAuthorResponse.parser()

    override fun request(request: UpdateAuthorRequest): Mono<UpdateAuthorResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
