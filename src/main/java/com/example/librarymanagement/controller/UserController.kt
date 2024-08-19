package com.example.librarymanagement.controller

import com.example.librarymanagement.annotation.NotificationOnAvailability
import com.example.librarymanagement.dto.JournalDto
import com.example.librarymanagement.dto.ReservationDto
import com.example.librarymanagement.dto.UserRequestDto
import com.example.librarymanagement.dto.UserResponseDto
import com.example.librarymanagement.dto.mapper.JournalMapper
import com.example.librarymanagement.dto.mapper.ReservationMapper
import com.example.librarymanagement.dto.mapper.UserMapper
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

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val journalMapper: JournalMapper,
    private val reservationMapper: ReservationMapper){

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun findById(@PathVariable id: String): UserResponseDto = userMapper.toUserResponseDto(userService.getUserById(id))

    @GetMapping(params = ["email", "phoneNumber"])
    @ResponseStatus(HttpStatus.OK)
    fun getUserByPhoneNumberOrEmail(@RequestParam(required = false) email: String,
                                    @RequestParam(required = false) phoneNumber: String): UserResponseDto {
        return userMapper.toUserResponseDto(userService.getUserByPhoneNumberOrEmail(email, phoneNumber))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody @Valid user: UserRequestDto): UserResponseDto {
        return userMapper.toUserResponseDto(userService.createUser(userMapper.toUser(user)))
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@PathVariable id: String, @RequestBody @Valid userRequestDto: UserRequestDto): UserResponseDto {
        return userMapper.toUserResponseDto(userService.updateUser(userMapper.toUser(userRequestDto, id)))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: String) = userService.deleteUser(id)

    @GetMapping("/{id}/reservations")
    @ResponseStatus(HttpStatus.OK)
    fun findReservationsByUser(@PathVariable id: String): List<ReservationDto> {
        return reservationMapper.toReservationDto(userService.findReservationsByUser(id))
    }

    @GetMapping("/{id}/journals")
    @ResponseStatus(HttpStatus.OK)
    fun findJournalsByUser(@PathVariable id: String): List<JournalDto> {
        return journalMapper.toJournalDto(userService.findJournalsByUser(id))
    }

    @PostMapping("/{id}/borrowings")
    @ResponseStatus(HttpStatus.CREATED)
    fun borrowBookFromLibrary(@PathVariable(name = "id") userId: String,
                              @RequestParam libraryId: String,
                              @RequestParam bookId: String): List<JournalDto> {
        return journalMapper.toJournalDto(userService.borrowBookFromLibrary(userId, libraryId, bookId))
    }

    @PostMapping("/{id}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    fun reserveBookInLibrary(@PathVariable(name = "id") userId: String,
                             @RequestParam(required = false) libraryId: String,
                             @RequestParam bookId: String): List<ReservationDto> {
        return reservationMapper.toReservationDto(userService.reserveBookInLibrary(userId, libraryId, bookId))
    }

    @DeleteMapping("/{id}/borrowings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @NotificationOnAvailability
    fun returnBookToLibrary(@PathVariable(name = "id") userId: String,
                            @RequestParam libraryId: String,
                            @RequestParam bookId: String) {
        throw RuntimeException()
        userService.returnBookToLibrary(userId, libraryId, bookId)
    }

    @DeleteMapping("/{userId}/reservations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelBookInLibrary(@PathVariable userId: String, @RequestParam(name = "id") bookId: String) {
        userService.cancelReservationInLibrary(userId, bookId)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllUsers(): List<UserResponseDto> = userMapper.toUserResponseDto(userService.findAll())
}
