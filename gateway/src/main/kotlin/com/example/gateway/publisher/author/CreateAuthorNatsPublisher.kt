package com.example.gateway.publisher.author

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.author.create.proto.CreateAuthorRequest
import com.example.internalapi.request.author.create.proto.CreateAuthorResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CreateAuthorNatsPublisher(
    private val connection: Connection
) : NatsPublisher<CreateAuthorRequest, CreateAuthorResponse> {
    override val subject = NatsSubject.Author.CREATE
    override val parser: Parser<CreateAuthorResponse> = CreateAuthorResponse.parser()

    override fun request(request: CreateAuthorRequest): Mono<CreateAuthorResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
