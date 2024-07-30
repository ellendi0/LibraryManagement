package com.example.librarymanagement.controller

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.data.TestDataFactory.TestDataRel
import com.example.librarymanagement.data.TestDataFactory.createTestDataRelForController
import com.example.librarymanagement.dto.JournalDto
import com.example.librarymanagement.dto.ReservationDto
import com.example.librarymanagement.dto.UserRequestDto
import com.example.librarymanagement.dto.UserResponseDto
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.dto.mapper.JournalMapper
import com.example.librarymanagement.dto.mapper.ReservationMapper
import com.example.librarymanagement.dto.mapper.UserMapper
import com.example.librarymanagement.exception.DuplicateKeyException
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.entity.Journal
import com.example.librarymanagement.model.entity.Reservation
import com.example.librarymanagement.model.entity.User
import com.example.librarymanagement.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class UserControllerTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var userService: UserService
    private lateinit var userController: UserController
    private lateinit var userMapper: UserMapper
    private lateinit var reservationMapper: ReservationMapper
    private lateinit var journalMapper: JournalMapper
    private lateinit var globalExceptionHandler: GlobalExceptionHandler
    private lateinit var errorMapper: ErrorMapper
    private val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())

    private var testData: TestDataRel = createTestDataRelForController()
    private var user = testData.user
    private var reservation = testData.reservation
    private var journal = testData.journal
    private lateinit var userRequestDto: UserRequestDto
    private lateinit var userResponseDto: UserResponseDto
    private lateinit var reservationDto: ReservationDto
    private lateinit var journalDto: JournalDto

    private var errorDto1 = ErrorMapper().toErrorDto(HttpStatus.BAD_REQUEST, "Invalid")
    private var errorDto2 = ErrorMapper().toErrorDto(HttpStatus.NOT_FOUND, "User")
    private var errorDto3 = ErrorMapper().toErrorDto(HttpStatus.CONFLICT, listOf("User", "email"))

    @BeforeEach
    fun setUp() {
        userService = mockk(relaxed = true)
        userController = mockk(relaxed = true)
        userMapper = mockk(relaxed = true)
        reservationMapper = mockk(relaxed = true)
        journalMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        userController = UserController(userService, userMapper, journalMapper, reservationMapper)

        userRequestDto = TestDataFactory.createUserRequestDto()
        userResponseDto = UserMapper().toUserResponseDto(user)
        reservationDto = ReservationMapper().toReservationDto(reservation)
        journalDto = JournalMapper().toJournalDto(journal)

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldCreateUser() {
        val expected = objectMapper.writeValueAsString(userResponseDto)

        every { userService.createUser(any()) } returns user
        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto

        val result = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotCreateWithInvalidEmail() {
        val userRequestNew = userRequestDto.copy(email = "first")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithInvalidFirstName() {
        val userRequestNew = userRequestDto.copy(firstName = "first")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithInvalidLastName() {
        val userRequestNew = userRequestDto.copy(lastName = "first")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithInvalidPassword() {
        val userRequestNew = userRequestDto.copy(password = "first")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithInvalidPhone() {
        val userRequestNew = userRequestDto.copy(phoneNumber = "first")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithEmptyEmail() {
        val userRequestNew = userRequestDto.copy(email = "")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithEmptyFirstName() {
        val userRequestNew = userRequestDto.copy(firstName = "")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithEmptyLastName() {
        val userRequestNew = userRequestDto.copy(lastName = "")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithEmptyPassword() {
        val userRequestNew = userRequestDto.copy(password = "")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithEmptyPhone() {
        val userRequestNew = userRequestDto.copy(phoneNumber = "")

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestNew)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldNotCreateWithDuplicateEmail() {
        every { userService.createUser(any()) } throws DuplicateKeyException("User", "email")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto3

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isConflict)
    }

    @Test
    fun shouldNotCreateWithDuplicatePhone() {
        every { userService.createUser(any()) } throws DuplicateKeyException("User", "phone")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto3

        mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isConflict)
    }

    @Test
    fun shouldUpdateUser() {
        val expected = objectMapper.writeValueAsString(userResponseDto)

        every { userService.updateUser(any(), any()) } returns user
        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto

        val result = mockMvc.perform(put("/api/v1/user/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotUpdateWithDuplicateEmail() {
        every { userService.updateUser(any(), any()) } throws DuplicateKeyException("User", "email")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto3

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isConflict)
    }

    @Test
    fun shouldNotUpdateWithDuplicatePhone() {
        every { userService.updateUser(any(), any()) } throws DuplicateKeyException("User", "phone")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto3

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isConflict)
    }

    @Test
    fun shouldGetUserById() {
        val expected = objectMapper.writeValueAsString(userResponseDto)

        every { userService.getUserById(any()) } returns user
        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto

        val result = mockMvc.perform(put("/api/v1/user/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotGetNotExistingUserById() {
        every { userService.getUserById(any()) } throws EntityNotFoundException("User")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(get("/api/v1/user/{id}", 0L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldGetReservationsByUserId() {
        val expected = objectMapper.writeValueAsString(listOf(reservationDto))

        every { userService.findReservationsByUser(any()) } returns listOf(reservation)
        every { reservationMapper.toReservationDto(any<List<Reservation>>()) } returns listOf(reservationDto)

        val result = mockMvc.perform(get("/api/v1/user/{id}/reservations", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotGetReservationsByUserId() {
        every { userService.findReservationsByUser(any()) } throws EntityNotFoundException("User")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/user/{id}/reservations", 0L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldGetJournalsByUserId() {
        val expected = objectMapper.writeValueAsString(listOf(journalDto))

        every { userService.findJournalsByUser(any()) } returns listOf(journal)
        every { journalMapper.toJournalDto(any<List<Journal>>()) } returns listOf(journalDto)

        val result = mockMvc.perform(get("/api/v1/user/{id}/journals", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotGetJournalsByUserId() {
        every { userService.findJournalsByUser(any()) } throws EntityNotFoundException("User")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(get("/api/v1/user/{id}/journals", 0L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldGetUserByPhoneNumberOrEmail() {
        val expected = objectMapper.writeValueAsString(userResponseDto)

        every { userService.getUserByPhoneNumberOrEmail(any(), any()) } returns user
        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto

        val result = mockMvc.perform(get("/api/v1/user")
            .param("phoneNumber", "1234567890")
            .param("email", "first@example.com")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotGetUserByPhoneNumberOrEmailWithInvalidData() {
        every { userService.getUserByPhoneNumberOrEmail(any(), any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto1

        mockMvc.perform(get("/api/v1/user")
            .param("phoneNumber", "123456789")
            .param("email", "first")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequestDto)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun shouldBorrowBookFromLibrary(){
        val expected = objectMapper.writeValueAsString(listOf(journalDto))

        every { userService.borrowBookFromLibrary(any(), any(), any()) } returns listOf(journal)
        every { journalMapper.toJournalDto(any<List<Journal>>()) } returns listOf(journalDto)

        val result = mockMvc.perform(post("/api/v1/user/{id}/borrowings", 1L)
            .param("libraryId", "1")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotBorrowBookFromLibraryWithNonExistingUser(){
        every { userService.borrowBookFromLibrary(any(), any(), any()) } throws EntityNotFoundException("User")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(post("/api/v1/user/{id}/borrowings", 0L)
            .param("libraryId", "1")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldNotBorrowBookFromLibraryWithNonExistingLibrary(){
        every { userService.borrowBookFromLibrary(any(), any(), any()) } throws EntityNotFoundException("Library")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(post("/api/v1/user/{id}/borrowings", 1L)
            .param("libraryId", "0")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldNotBorrowBookFromLibraryWithNonExistingBook(){
        every { userService.borrowBookFromLibrary(any(), any(), any()) } throws EntityNotFoundException("Book")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(post("/api/v1/user/{id}/borrowings", 1L)
            .param("libraryId", "1")
            .param("bookId", "0")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldReturnBookToLibrary(){
        val expected = objectMapper.writeValueAsString(listOf(journalDto))

        every { userService.returnBookToLibrary(any(), any(), any()) } returns listOf(journal)
        every { journalMapper.toJournalDto(any<List<Journal>>()) } returns listOf(journalDto)

        val result = mockMvc.perform(delete("/api/v1/user/{id}/borrowings", 1L)
            .param("libraryId", "1")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotReturnBookToLibraryWithNonExistingUser(){
        every { userService.returnBookToLibrary(any(), any(), any()) } throws EntityNotFoundException("User")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(delete("/api/v1/user/{id}/borrowings", 0L)
            .param("libraryId", "1")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldReserveBookInLibrary(){
        val expected = objectMapper.writeValueAsString(listOf(reservationDto))

        every { userService.reserveBookInLibrary(any(), any(), any()) } returns listOf(reservation)
        every { reservationMapper.toReservationDto(any<List<Reservation>>()) } returns listOf(reservationDto)

        val result = mockMvc.perform(post("/api/v1/user/{id}/reservations", 1L)
            .param("libraryId", "1")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotReserveBookInLibraryWithNonExistingUser(){
        every { userService.reserveBookInLibrary(any(), any(), any()) } throws EntityNotFoundException("User")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(post("/api/v1/user/{id}/reservations", 0L)
            .param("libraryId", "1")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldNotReserveBookInLibraryWithNonExistingLibrary(){
        every { userService.reserveBookInLibrary(any(), any(), any()) } throws EntityNotFoundException("Library")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(post("/api/v1/user/{id}/reservations", 1L)
            .param("libraryId", "0")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldNotReserveBookInLibraryWithNonExistingBook(){
        every { userService.reserveBookInLibrary(any(), any(), any()) } throws EntityNotFoundException("Book")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto2

        mockMvc.perform(post("/api/v1/user/{id}/reservations", 1L)
            .param("libraryId", "1")
            .param("bookId", "0")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun shouldCancelReservation(){
        every { userService.cancelReservationInLibrary(any(), any()) } returns Unit

        mockMvc.perform(delete("/api/v1/user/{id}/reservations", 1L)
            .param("id", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
    }
}