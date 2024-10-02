package com.example.internalapi

object NatsSubject {
    private const val REQUEST_PREFIX = "com.example.internalapi.input.request"

    object Author {
        private const val AUTHOR_PREFIX = "$REQUEST_PREFIX.author"

        const val CREATE = "$AUTHOR_PREFIX.create"
        const val GET_BY_ID = "$AUTHOR_PREFIX.get_by_id"
        const val GET_ALL = "$AUTHOR_PREFIX.get_all"
        const val UPDATE = "$AUTHOR_PREFIX.update"
    }

    object Book {
        private const val BOOK_PREFIX = "$REQUEST_PREFIX.book"

        const val CREATE = "$BOOK_PREFIX.create"
        const val GET_BY_ID = "$BOOK_PREFIX.get_by_id"
        const val GET_ALL = "$BOOK_PREFIX.get_all"
        const val UPDATE = "$BOOK_PREFIX.update"
        const val DELETE = "$BOOK_PREFIX.delete"
        const val GET_BY_TITLE_AND_AUTHOR = "$BOOK_PREFIX.get_by_title_and_author"
    }

    object BookPresence {
        private const val BOOK_PRESENCE_REQUEST = "$REQUEST_PREFIX.book_presence"

        const val ADD_TO_LIBRARY = "$BOOK_PRESENCE_REQUEST.add_to_library"
        const val BORROW_FROM_LIBRARY = "$BOOK_PRESENCE_REQUEST.borrow_from_library"
        const val GET_ALL_BY_LIBRARY_ID = "$BOOK_PRESENCE_REQUEST.get_all_by_library_id"
        const val GET_ALL_BY_LIBRARY_ID_AND_BOOK_ID = "$BOOK_PRESENCE_REQUEST.get_all_by_library_id_and_book_id"
        const val REMOVE_FROM_LIBRARY = "$BOOK_PRESENCE_REQUEST.remove_from_library"
        const val RETURN_TO_LIBRARY = "$BOOK_PRESENCE_REQUEST.return_to_library"
    }

    object Journal {
        private const val JOURNAL_PREFIX = "$REQUEST_PREFIX.journal"

        const val GET_ALL_BY_USER_ID = "$REQUEST_PREFIX.get_all_by_user_id"
    }

    object Library {
        private const val LIBRARY_PREFIX = "$REQUEST_PREFIX.library"

        const val CREATE = "$LIBRARY_PREFIX.create"
        const val GET_ALL = "$LIBRARY_PREFIX.get_all"
        const val UPDATE = "$LIBRARY_PREFIX.update"
        const val DELETE = "$LIBRARY_PREFIX.delete"
        const val GET_BY_ID = "$LIBRARY_PREFIX.get_by_id"
    }

    object Publisher {
        private const val PUBLISHER_PREFIX = "$REQUEST_PREFIX.publisher"

        const val CREATE = "$PUBLISHER_PREFIX.create"
        const val GET_BY_ID = "$PUBLISHER_PREFIX.get_by_id"
        const val GET_ALL = "$PUBLISHER_PREFIX.get_all"
        const val UPDATE = "$PUBLISHER_PREFIX.update"
    }

    object Reservation {
        private const val RESERVATION_PREFIX = "$REQUEST_PREFIX.reservation"

        const val CREATE = "$RESERVATION_PREFIX.create"
        const val GET_ALL_BY_USER_ID = "$RESERVATION_PREFIX.get_all"
        const val DELETE = "$RESERVATION_PREFIX.delete"
        const val NOTIFY_ON_AVAILABILITY = "$RESERVATION_PREFIX.notify_availability"
    }

    object User {
        private const val USER_PREFIX = "$REQUEST_PREFIX.user"

        const val CREATE = "$USER_PREFIX.create"
        const val GET_ALL = "$USER_PREFIX.get_all"
        const val UPDATE = "$USER_PREFIX.update"
        const val DELETE = "$USER_PREFIX.delete"
        const val GET_BY_ID = "$USER_PREFIX.get_by_id"
        const val GET_BY_EMAIL_OR_PHONE_NUMBER = "$USER_PREFIX.get_by_email_or_phone_number"
    }
}
