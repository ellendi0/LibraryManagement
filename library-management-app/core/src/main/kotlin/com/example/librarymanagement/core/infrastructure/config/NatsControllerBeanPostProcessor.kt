package com.example.librarymanagement.core.infrastructure.config

import com.example.librarymanagement.core.infrastructure.nats.NatsController
import com.google.protobuf.GeneratedMessageV3
import io.nats.client.Connection
import io.nats.client.Dispatcher
import io.nats.client.Message
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class NatsControllerBeanPostProcessor(private val connection: Connection) : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsController<*, *>) {
            bean.initializeNatsController()
        }
        return bean
    }

    private fun NatsController<*, *>.initializeNatsController() {
        createDispatcher(connection).apply {
            subscribe(subject)
        }
    }

    private fun <RequestT : GeneratedMessageV3, ResponseT : GeneratedMessageV3>
            NatsController<RequestT, ResponseT>.createDispatcher(
        connection: Connection
    ): Dispatcher =
        connection.createDispatcher { message: Message ->
            val parsedData = parser.parseFrom(message.data)
            val response = handle(parsedData)
            response.map { connection.publish(message.replyTo, it.toByteArray()) }
                .subscribe()
        }
}
