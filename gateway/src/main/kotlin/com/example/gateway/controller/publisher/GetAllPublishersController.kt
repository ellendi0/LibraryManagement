package com.example.com.example.gateway.controller.publisher

import com.example.com.example.gateway.publisher.publisher.GetAllPublishersNatsPublisher
import com.example.gateway.model.PublisherDto
import com.example.gateway.model.mapper.PublisherMapper
import com.example.internalapi.request.publisher.create.proto.GetAllPublishersRequest
import com.example.internalapi.request.publisher.create.proto.GetAllPublishersResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/v2/publisher")
class GetAllPublishersController(
    private val getAllPublishersNatsPublisher: GetAllPublishersNatsPublisher,
    private val publisherMapper: PublisherMapper
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllPublishers(): Flux<PublisherDto> {
        return getAllPublishersNatsPublisher.request(GetAllPublishersRequest.getDefaultInstance())
            .flatMapMany { response ->
                if (response.hasSuccess()) {
                    handleSuccess(response)
                } else {
                    Flux.empty()
                }
            }

    }

    private fun handleSuccess(response: GetAllPublishersResponse): Flux<PublisherDto> {
        val publishersList = response.success.publishers.publishersList
        return Flux.fromIterable(publishersList)
            .map { publisherMapper.toPublisherDto(it) }
    }
}
