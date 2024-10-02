package com.example.gateway.controller.grpc

import com.example.com.example.gateway.validation.PublisherRequestValidation
import com.example.gateway.publisher.publisher.CreatePublisherNatsPublisher
import com.example.gateway.publisher.publisher.GetAllPublishersNatsPublisher
import com.example.gateway.publisher.publisher.GetPublisherByIdNatsPublisher
import com.example.gateway.publisher.publisher.UpdatePublisherNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorPublisherServiceGrpc
import com.example.internalapi.request.publisher.create.proto.CreatePublisherRequest
import com.example.internalapi.request.publisher.create.proto.CreatePublisherResponse
import com.example.internalapi.request.publisher.get_all.proto.GetAllPublishersRequest
import com.example.internalapi.request.publisher.get_all.proto.GetAllPublishersResponse
import com.example.internalapi.request.publisher.get_by_id.proto.GetPublisherByIdRequest
import com.example.internalapi.request.publisher.get_by_id.proto.GetPublisherByIdResponse
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherRequest
import com.example.internalapi.request.publisher.update.proto.UpdatePublisherResponse
import reactor.core.publisher.Mono

@GrpcService
class PublisherService(
    private val createPublisherNatsPublisher: CreatePublisherNatsPublisher,
    private val getAllPublishersNatsPublisher: GetAllPublishersNatsPublisher,
    private val getPublisherByIdNatsPublisher: GetPublisherByIdNatsPublisher,
    private val updatePublisherNatsPublisher: UpdatePublisherNatsPublisher,
    private val validation: PublisherRequestValidation
) : ReactorPublisherServiceGrpc.PublisherServiceImplBase() {

    override fun createPublisher(request: Mono<CreatePublisherRequest>): Mono<CreatePublisherResponse> =
        request
            .flatMap { validation.validate(it) }
            .flatMap { publisher -> createPublisherNatsPublisher.request(publisher) }

    override fun getAllPublishers(request: Mono<GetAllPublishersRequest>): Mono<GetAllPublishersResponse> =
        request.flatMap { publisher -> getAllPublishersNatsPublisher.request(publisher) }

    override fun getPublisherById(request: Mono<GetPublisherByIdRequest>): Mono<GetPublisherByIdResponse> =
        request
            .flatMap { publisher ->
                getPublisherByIdNatsPublisher.request(
                    GetPublisherByIdRequest.newBuilder().apply { id = publisher.id }.build()
                )
            }

    override fun updatePublisher(request: Mono<UpdatePublisherRequest>): Mono<UpdatePublisherResponse> =
        request
            .flatMap { validation.validate(it) }
            .flatMap { publisher -> updatePublisherNatsPublisher.request(publisher) }
}
