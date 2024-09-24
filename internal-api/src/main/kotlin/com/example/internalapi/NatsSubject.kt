package com.example.internalapi

object NatsSubject {
    private const val REQUEST_PREFIX = "com.example.internalapi.input.request"

    object Publisher {
        private const val PUBLISHER_PREFIX = "$REQUEST_PREFIX.publisher"

        const val CREATE = "$PUBLISHER_PREFIX.create"
        const val GET_BY_ID = "$PUBLISHER_PREFIX.get_by_id"
        const val GET_ALL = "$PUBLISHER_PREFIX.get_all"
        const val UPDATE = "$PUBLISHER_PREFIX.update"
    }
}
