package com.example.librarymanagement.controller

import com.example.librarymanagement.data.ErrorDataFactory
import com.example.librarymanagement.data.TestDataFactory.TestDataRel
import com.example.librarymanagement.data.TestDataFactory.createTestDataRelForController
import com.example.librarymanagement.data.UserDataFactory
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.dto.mapper.JournalMapper
import com.example.librarymanagement.dto.mapper.ReservationMapper
import com.example.librarymanagement.dto.mapper.UserMapper
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
    private var userRequestDto = UserDataFactory.createUserRequestDto()
    private var userResponseDto = UserMapper().toUserResponseDto(user)
    private var reservationDto = ReservationMapper().toReservationDto(reservation)
    private var journalDto = JournalMapper().toJournalDto(journal)

    private var errorBadRequest = ErrorDataFactory.createBadRequestError()

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

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldCreateUser() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(userResponseDto)
        val content = objectMapper.writeValueAsString(userRequestDto)

        every { userService.createUser(any()) } returns user
        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithInvalidEmail() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(email = "first"))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithInvalidFirstName() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(firstName = "first"))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        //WHEN
        val actual =  mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithInvalidLastName() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(lastName = "first"))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithInvalidPassword() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(password = "first"))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithInvalidPhone() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(phoneNumber = "123456789"))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithEmptyEmail() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(email = ""))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithEmptyFirstName() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(firstName = ""))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithEmptyLastName() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(lastName = ""))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithEmptyPassword() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(password = ""))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateWithEmptyPhone() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto.copy(phoneNumber = ""))
        val expected = objectMapper.writeValueAsString(errorBadRequest)

        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldUpdateUser() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(userResponseDto)
        val content = objectMapper.writeValueAsString(userRequestDto)

        every { userService.updateUser(any()) } returns user
        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/user/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetUserById() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(userResponseDto)
        val content = objectMapper.writeValueAsString(userRequestDto)

        every { userService.getUserById(any()) } returns user
        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/user/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetReservationsByUserId() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(reservationDto))
        val content = objectMapper.writeValueAsString(userRequestDto)

        every { userService.findReservationsByUser(any()) } returns listOf(reservation)
        every { reservationMapper.toReservationDto(any<List<Reservation>>()) } returns listOf(reservationDto)

        val actual = mockMvc.perform(get("/api/v1/user/{id}/reservations", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetJournalsByUserId() {
        //GIVEN
        val content = objectMapper.writeValueAsString(userRequestDto)
        val expected = objectMapper.writeValueAsString(listOf(journalDto))

        every { userService.findJournalsByUser(any()) } returns listOf(journal)
        every { journalMapper.toJournalDto(any<List<Journal>>()) } returns listOf(journalDto)

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/user/{id}/journals", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetUserByPhoneNumberOrEmail() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(userResponseDto)

        every { userService.getUserByPhoneNumberOrEmail(any(), any()) } returns user
        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto

        //WHEN
        val actual = mockMvc.perform(get("/api/v1/user")
            .param("phoneNumber", "1234567890")
            .param("email", "first@example.com")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldBorrowBookFromLibrary(){
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(journalDto))

        every { userService.borrowBookFromLibrary(any(), any(), any()) } returns listOf(journal)
        every { journalMapper.toJournalDto(any<List<Journal>>()) } returns listOf(journalDto)

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user/{id}/borrowings", 1L)
            .param("libraryId", "1")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnBookToLibrary(){
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(journalDto))

        every { userService.returnBookToLibrary(any(), any(), any()) } returns listOf(journal)
        every { journalMapper.toJournalDto(any<List<Journal>>()) } returns listOf(journalDto)

        //WHEN
        val actual = mockMvc.perform(delete("/api/v1/user/{id}/borrowings", 1L)
            .param("libraryId", "1")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldReserveBookInLibrary(){
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(reservationDto))

        every { userService.reserveBookInLibrary(any(), any(), any()) } returns listOf(reservation)
        every { reservationMapper.toReservationDto(any<List<Reservation>>()) } returns listOf(reservationDto)

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/user/{id}/reservations", 1L)
            .param("libraryId", "1")
            .param("bookId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldCancelReservation(){
        //GIVEN
        val expected = 204

        every { userService.cancelReservationInLibrary(any(), any()) } returns Unit

        //WHEN
        val actual = mockMvc.perform(delete("/api/v1/user/{id}/reservations", 1L)
            .param("id", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andReturn().response.status

        //THEN
        Assertions.assertEquals(expected, actual)
    }
}
