package com.example.gateway.publisher.library

import com.example.gateway.publisher.NatsPublisher
import com.example.internalapi.NatsSubject
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdRequest
import com.example.internalapi.request.library.get_by_id.proto.GetLibraryByIdResponse
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetLibraryByIdNatsPublisher(
    private val connection: Connection
) : NatsPublisher<GetLibraryByIdRequest, GetLibraryByIdResponse> {
    override val subject = NatsSubject.Library.GET_BY_ID
    override val parser: Parser<GetLibraryByIdResponse> = GetLibraryByIdResponse.parser()

    override fun request(request: GetLibraryByIdRequest): Mono<GetLibraryByIdResponse> {
        return Mono.fromFuture { connection.request(subject, request.toByteArray()) }
            .map { parser.parseFrom(it.data) }
    }
}
