package com.example.gateway.publisher.library

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.library.get_all.proto.GetAllLibrariesRequest
import com.example.internalapi.request.library.get_all.proto.GetAllLibrariesResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllLibraryNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetAllLibrariesRequest, GetAllLibrariesResponse> {
    override val subject = NatsSubject.Library.GET_ALL
    override val parser: Parser<GetAllLibrariesResponse> = GetAllLibrariesResponse.parser()

    override fun request(request: GetAllLibrariesRequest): Mono<GetAllLibrariesResponse> {
        return Mono.fromFuture { connection.request(subject, null) }
            .map { parser.parseFrom(it.data) }
    }
}
