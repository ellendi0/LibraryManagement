package com.example.librarymanagement.data

import com.example.librarymanagement.dto.BookRequestDto
import com.example.librarymanagement.dto.UserRequestDto
import com.example.librarymanagement.model.entity.Author
import com.example.librarymanagement.model.entity.Book
import com.example.librarymanagement.model.entity.BookPresence
import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.model.entity.Library
import com.example.librarymanagement.model.entity.Publisher
import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User
import com.example.librarymanagement.model.enums.Availability
import com.example.librarymanagement.model.enums.Genre
import java.time.LocalDate

object TestDataFactory {

    fun createLibrary(): Library = Library(1L, "Library", "Address")

    fun createPublisher(): Publisher = Publisher(1L, "Publisher")

    fun createAuthor(): Author = Author(1L, "Author", "Author")

    fun createBook(): Book {
        val author = createAuthor()
        val publisher = createPublisher()
        return Book(
            id = 1L,
            title = "Book",
            author = author,
            publisher = publisher,
            publishedYear = 2021,
            genre = Genre.DRAMA,
            isbn = 1234567890123)
    }

    fun createUser(): User {
        return User(
            id = 1L,
            firstName = "First",
            lastName = "First",
            email = "first@email.com",
            phoneNumber = "1234567890",
            password = "Password1"
        )
    }

    fun createBookPresence(): BookPresence {
        val book = createBook()
        val library = createLibrary()
        val user = createUser()
        return BookPresence(
            id = 1L,
            availability = Availability.AVAILABLE,
            book = book,
            library = library,
            user = user
        )
    }

    fun createJournal(user: User, bookPresence: BookPresence): Journal {
        return Journal(
            id = 1L,
            user = user,
            bookPresence = bookPresence,
            dateOfBorrowing = LocalDate.now(),
            dateOfReturning = LocalDate.now().plusDays(7)
        )
    }

    fun createJournalWithoutUser(): Journal {
        val bookPresence = createBookPresence()
        return Journal(
            id = 1L,
            user = null,
            bookPresence = bookPresence,
            dateOfBorrowing = LocalDate.now(),
            dateOfReturning = LocalDate.now().plusDays(7)
        )
    }

    fun createJournalWithoutUserAndBookPresence(): Journal {
        return Journal(
            id = 1L,
            user = null,
            bookPresence = null,
            dateOfBorrowing = LocalDate.now(),
            dateOfReturning = LocalDate.now().plusDays(7)
        )
    }

    fun createReservation(book: Book, user: User, library: Library): Reservation {
        return Reservation(
            id = 1L,
            book = book,
            user = user,
            library = library
        )
    }

    fun createReservationWithoutUser(): Reservation {
        val book = createBook()
        val library = createLibrary()
        return Reservation(
            id = 1L,
            book = book,
            user = null,
            library = library
        )
    }

    fun createReservationWithoutBook(): Reservation {
        val user = createUser()
        val library = createLibrary()
        return Reservation(
            id = 1L,
            book = null,
            user = user,
            library = library
        )
    }

    fun createUserRequestDto(): UserRequestDto {
        return UserRequestDto(
            firstName = "First",
            lastName = "First",
            email = "first@example.com",
            phoneNumber = "1234567890",
            password = "Password1"
        )
    }

    fun createBookRequestDto(): BookRequestDto {
        return BookRequestDto(
            title = "Book",
            authorId = 1L,
            publisherId = 1L,
            publishedYear = 2021,
            genre = Genre.DRAMA,
            isbn = 1234567890123L
        )
    }

    fun createTestDataRelForServices(): TestDataRel {
        val library = createLibrary()
        val user = createUser()
        val book = createBook()
        val bookPresence = createBookPresence()
        val journal = createJournalWithoutUserAndBookPresence()
        val reservation = createReservationWithoutUser()

        user.journals = mutableListOf(journal)
        user.reservations = mutableListOf(reservation)
        book.bookPresence = mutableListOf(bookPresence)
        book.reservations = mutableListOf(reservation)
        bookPresence.journals = mutableListOf(journal)
        bookPresence.user = user
        library.bookPresence = mutableListOf(bookPresence)

        return TestDataRel(library, user, book, bookPresence, journal, reservation)
    }

    fun createTestDataRelForController(): TestDataRel {
        val library = createLibrary()
        val user = createUser()
        val book = createBook()
        val bookPresence = createBookPresence()
        val journal = createJournal(user, bookPresence)
        val reservation = createReservation(book, user, library)

        user.journals = mutableListOf(journal)
        user.reservations = mutableListOf(reservation)
        book.bookPresence = mutableListOf(bookPresence)
        book.reservations = mutableListOf(reservation)
        bookPresence.journals = mutableListOf(journal)
        bookPresence.user = user
        library.bookPresence = mutableListOf(bookPresence)

        return TestDataRel(library, user, book, bookPresence, journal, reservation)
    }

    data class TestDataRel(
        val library: Library,
        val user: User,
        val book: Book,
        val bookPresence: BookPresence,
        val journal: Journal,
        val reservation: Reservation
    )
}
