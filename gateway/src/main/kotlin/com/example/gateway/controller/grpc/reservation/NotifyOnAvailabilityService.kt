package com.example.com.example.gateway.controller.grpc.reservation

import com.example.com.example.gateway.subcriber.NotifyOnAvailabilityNatsSubscriber
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorNotifyOnAvailabilityServiceGrpc
import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityRequest
import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@GrpcService
class NotifyOnAvailabilityService(
    private val notifyOnAvailabilityNatsSubscriber: NotifyOnAvailabilityNatsSubscriber
) : ReactorNotifyOnAvailabilityServiceGrpc.NotifyOnAvailabilityServiceImplBase() {

    override fun notifyOnAvailability(request: Mono<NotifyOnAvailabilityRequest>): Flux<NotifyOnAvailabilityResponse> {
        return request.flatMapMany { notifyOnAvailabilityNatsSubscriber.subscribe(request) }
    }
}
