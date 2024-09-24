package com.example.librarymanagement.controller.nats.publisher

import com.example.internalapi.NatsSubject.Publisher.GET_ALL
import com.example.internalapi.model.Publisher
import com.example.internalapi.request.publisher.create.proto.GetAllPublishersRequest
import com.example.internalapi.request.publisher.create.proto.GetAllPublishersResponse
import com.example.librarymanagement.controller.nats.NatsController
import com.example.librarymanagement.dto.mapper.nats.PublisherMapper
import com.example.librarymanagement.service.PublisherService
import com.google.protobuf.Parser
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllPublishersNatsController(
    private val publisherService: PublisherService,
    private val publisherMapper: PublisherMapper
) : NatsController<GetAllPublishersRequest, GetAllPublishersResponse> {
    override val subject = GET_ALL
    override val parser: Parser<GetAllPublishersRequest> = GetAllPublishersRequest.parser()

    override fun handle(request: GetAllPublishersRequest): Mono<GetAllPublishersResponse> {
        return publisherService.getAllPublishers()
            .map { publisherMapper.toPublisherProto(it) }
            .collectList()
            .map { buildSuccessResponse(it) }
    }

    private fun buildSuccessResponse(publisher: List<Publisher>): GetAllPublishersResponse {
        return GetAllPublishersResponse.newBuilder()
            .apply { successBuilder.publishersBuilder.addAllPublishers(publisher) }
            .build()
    }
}
