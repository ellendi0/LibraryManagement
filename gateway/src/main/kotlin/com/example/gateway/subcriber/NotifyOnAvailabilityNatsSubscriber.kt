package com.example.com.example.gateway.subcriber

import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityRequest
import com.example.internalapi.request.reservation.notify_on_availability.proto.NotifyOnAvailabilityResponse
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class NotifyOnAvailabilityNatsSubscriber(val connection: Connection) {
    fun subscribe(request: Mono<NotifyOnAvailabilityRequest>): Flux<NotifyOnAvailabilityResponse> {
        return request.flatMapMany { req ->
            Flux.create { sink ->
                connection.createDispatcher()
                    .apply {
                        subscribe("reservation.${req.bookId}.${req.libraryId}") { message ->
                            val parsedData = NotifyOnAvailabilityResponse.parser().parseFrom(message.data)
                            sink.next(parsedData)
                        }
                    }
            }
        }
    }
}
