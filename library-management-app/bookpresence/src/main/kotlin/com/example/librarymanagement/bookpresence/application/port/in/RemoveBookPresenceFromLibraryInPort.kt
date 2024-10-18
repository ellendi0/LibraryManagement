package com.example.librarymanagement.bookpresence.application.port.`in`

import reactor.core.publisher.Mono

interface RemoveBookPresenceFromLibraryInPort {
    fun deleteBookPresenceById(id: String): Mono<Unit>
}
