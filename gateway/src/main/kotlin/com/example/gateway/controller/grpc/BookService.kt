package com.example.com.example.gateway.controller.grpc

import com.example.com.example.gateway.validation.BookRequestValidation
import com.example.gateway.publisher.book.CreateBookNatsPublisher
import com.example.gateway.publisher.book.DeleteBookNatsPublisher
import com.example.gateway.publisher.book.GetAllBookNatsPublisher
import com.example.gateway.publisher.book.GetBookByIdNatsPublisher
import com.example.gateway.publisher.book.GetBookByTitleAndAuthorNatsPublisher
import com.example.gateway.publisher.book.UpdateBookNatsPublisher
import com.example.grpcserverstarter.GrpcService
import com.example.internalapi.ReactorBookServiceGrpc
import com.example.internalapi.request.book.create.proto.CreateBookRequest
import com.example.internalapi.request.book.create.proto.CreateBookResponse
import com.example.internalapi.request.book.delete.proto.DeleteBookRequest
import com.example.internalapi.request.book.delete.proto.DeleteBookResponse
import com.example.internalapi.request.book.get_all.proto.GetAllBooksRequest
import com.example.internalapi.request.book.get_all.proto.GetAllBooksResponse
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdRequest
import com.example.internalapi.request.book.get_by_id.proto.GetBookByIdResponse
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorRequest
import com.example.internalapi.request.book.get_by_title_and_author.proto.GetBookByTitleAndAuthorResponse
import com.example.internalapi.request.book.update.proto.UpdateBookRequest
import com.example.internalapi.request.book.update.proto.UpdateBookResponse
import reactor.core.publisher.Mono

@GrpcService
class BookService(
    private val createBookNatsBook: CreateBookNatsPublisher,
    private val getAllBooksNatsBook: GetAllBookNatsPublisher,
    private val getBookByIdNatsBook: GetBookByIdNatsPublisher,
    private val updateBookNatsBook: UpdateBookNatsPublisher,
    private val deleteBookNatsBook: DeleteBookNatsPublisher,
    private val getBookByTitleAndAuthorNatsPublisher: GetBookByTitleAndAuthorNatsPublisher,
    private val validator: BookRequestValidation
) : ReactorBookServiceGrpc.BookServiceImplBase() {

    override fun createBook(request: Mono<CreateBookRequest>): Mono<CreateBookResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { book -> createBookNatsBook.request(book) }
    }

    override fun getAllBooks(request: Mono<GetAllBooksRequest>): Mono<GetAllBooksResponse> =
        request.flatMap { book -> getAllBooksNatsBook.request(book) }

    override fun getBookById(request: Mono<GetBookByIdRequest>): Mono<GetBookByIdResponse> {
        return request.flatMap { book ->
            getBookByIdNatsBook.request(
                GetBookByIdRequest.newBuilder().apply { id = book.id }.build()
            )
        }
    }

    override fun updateBook(request: Mono<UpdateBookRequest>): Mono<UpdateBookResponse> {
        return request
            .flatMap { validator.validate(it) }
            .flatMap { book -> updateBookNatsBook.request(book) }
    }

    override fun deleteBook(request: Mono<DeleteBookRequest>): Mono<DeleteBookResponse> =
        request.flatMap { book -> deleteBookNatsBook.request(book) }

    override fun getBookByTitleAndAuthor(
        request: Mono<GetBookByTitleAndAuthorRequest>
    ): Mono<GetBookByTitleAndAuthorResponse> =
        request.flatMap { book -> getBookByTitleAndAuthorNatsPublisher.request(book) }
}
