package com.example.gateway.publisher.library

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.library.find_all.proto.FindAllLibrariesRequest
import com.example.internalapi.request.library.find_all.proto.FindAllLibrariesResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllLibraryNatsPublisher(
    private val connection: Connection
) : NatsPublisher<FindAllLibrariesRequest, FindAllLibrariesResponse> {
    override val subject = NatsSubject.Library.FIND_ALL
    override val parser: Parser<FindAllLibrariesResponse> = FindAllLibrariesResponse.parser()

    override fun request(request: FindAllLibrariesRequest): Mono<FindAllLibrariesResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
