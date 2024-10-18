package com.example.librarymanagement.publisher.infrastructure.nats

import com.example.internalapi.NatsSubject.Publisher.FIND_ALL
import com.example.internalapi.model.Publisher
import com.example.internalapi.request.publisher.find_all.proto.FindAllPublishersRequest
import com.example.internalapi.request.publisher.find_all.proto.FindAllPublishersResponse
import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.example.librarymanagement.publisher.application.port.`in`.FindAllPublishersInPort
import com.example.librarymanagement.publisher.infrastructure.convertor.PublisherMapper
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FindAllPublishersNatsController(
    private val findAllPublishersInPort: FindAllPublishersInPort,
    private val publisherMapper: PublisherMapper
) : NatsController<FindAllPublishersRequest, FindAllPublishersResponse> {
    override val subject = FIND_ALL
    override val parser: Parser<FindAllPublishersRequest> = FindAllPublishersRequest.parser()

    override fun handle(request: FindAllPublishersRequest): Mono<FindAllPublishersResponse> {
        return findAllPublishersInPort.findAllPublishers()
            .map { publisherMapper.toPublisherProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(publisher: List<Publisher>): FindAllPublishersResponse {
        return FindAllPublishersResponse.newBuilder()
            .apply { successBuilder.publishersBuilder.addAllPublishers(publisher) }
            .build()
    }
}
