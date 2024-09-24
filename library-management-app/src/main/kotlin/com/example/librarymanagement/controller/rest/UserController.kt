package com.example.librarymanagement.controller.rest

import com.example.librarymanagement.dto.JournalDto
import com.example.librarymanagement.dto.ReservationDto
import com.example.librarymanagement.dto.UserRequestDto
import com.example.librarymanagement.dto.UserResponseDto
import com.example.librarymanagement.dto.mapper.JournalMapper
import com.example.librarymanagement.dto.mapper.ReservationMapper
import com.example.librarymanagement.dto.mapper.UserMapper
import com.example.librarymanagement.model.domain.ReservationOutcome
import com.example.librarymanagement.service.ReservationService
import com.example.librarymanagement.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
    private val reservationService: ReservationService,
    private val userMapper: UserMapper,
    private val journalMapper: JournalMapper,
    private val reservationMapper: ReservationMapper
) {
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable id: String): Mono<UserResponseDto> =
        userService.getUserById(id).map { userMapper.toUserResponseDto(it) }

    @GetMapping(params = ["email", "phoneNumber"])
    @ResponseStatus(HttpStatus.OK)
    fun getUserByPhoneNumberOrEmail(
        @RequestParam(required = false) email: String,
        @RequestParam(required = false) phoneNumber: String
    ): Mono<UserResponseDto> {
        return userService.getUserByPhoneNumberOrEmail(email, phoneNumber).map { userMapper.toUserResponseDto(it) }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody @Valid userRequestDto: UserRequestDto): Mono<UserResponseDto> {
        return userService.createUser(userMapper.toUser(userRequestDto)).map { userMapper.toUserResponseDto(it) }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(
        @PathVariable id: String,
        @RequestBody @Valid userRequestDto: UserRequestDto
    ): Mono<UserResponseDto> {
        return userService.updateUser(userMapper.toUser(userRequestDto, id)).map { userMapper.toUserResponseDto(it) }
    }

    @GetMapping("/{id}/reservations")
    @ResponseStatus(HttpStatus.OK)
    fun findReservationsByUser(@PathVariable id: String): Flux<ReservationDto> {
        return reservationService.getAllReservationsByUserId(id).map { reservationMapper.toReservationDto(it) }
    }

    @GetMapping("/{id}/journals")
    @ResponseStatus(HttpStatus.OK)
    fun findJournalsByUser(@PathVariable id: String): Flux<JournalDto> {
        return userService.findJournalsByUser(id).map { journalMapper.toJournalDto(it) }
    }

    @PostMapping("/{id}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    fun reserveBookInLibrary(
        @PathVariable(name = "id") userId: String,
        @RequestParam libraryId: String,
        @RequestParam bookId: String
    ): Flux<Any> =
        reservationService.reserveBook(userId, libraryId, bookId)
            .flatMapMany {
                when (it) {
                    is ReservationOutcome.Journals ->
                        Flux.fromIterable(journalMapper.toJournalDto(it.journals))

                    is ReservationOutcome.Reservations ->
                        Flux.fromIterable(reservationMapper.toReservationDto(it.reservations))
                }
            }

    @DeleteMapping("/{userId}/reservations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelBookInLibrary(@PathVariable userId: String, @RequestParam(name = "id") bookId: String): Mono<Unit> {
        return reservationService.cancelReservation(userId, bookId)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllUsers(): Flux<UserResponseDto> = userService.findAll().map { userMapper.toUserResponseDto(it) }
}
