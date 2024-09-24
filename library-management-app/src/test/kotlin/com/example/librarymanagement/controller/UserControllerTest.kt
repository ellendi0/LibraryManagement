//package com.example.librarymanagement.controller
//
//import com.example.librarymanagement.data.ErrorDataFactory
//import com.example.librarymanagement.data.JournalDataFactory
//import com.example.librarymanagement.data.ReservationDataFactory
//import com.example.librarymanagement.data.UserDataFactory
//import com.example.librarymanagement.dto.mapper.ErrorMapper
//import com.example.librarymanagement.dto.mapper.JournalMapper
//import com.example.librarymanagement.dto.mapper.ReservationMapper
//import com.example.librarymanagement.dto.mapper.UserMapper
//import com.example.librarymanagement.exception.GlobalExceptionHandler
//import com.example.librarymanagement.model.domain.Journal
//import com.example.librarymanagement.model.domain.Reservation
//import com.example.librarymanagement.model.domain.ReservationOutcome
//import com.example.librarymanagement.model.domain.User
//import com.example.librarymanagement.service.JournalService
//import com.example.librarymanagement.service.ReservationService
//import com.example.librarymanagement.service.UserService
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
//import io.mockk.every
//import io.mockk.mockk
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.http.MediaType
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import org.springframework.test.web.servlet.setup.MockMvcBuilders
//
//class UserControllerTest {
//    private lateinit var mockMvc: MockMvc
//
//    private val userService: UserService = mockk(relaxed = true)
//    private val userMapper: UserMapper = mockk(relaxed = true)
//    private val reservationMapper: ReservationMapper = mockk(relaxed = true)
//    private val reservationService: ReservationService = mockk()
//    private val journalService: JournalService = mockk()
//    private val journalMapper: JournalMapper = mockk(relaxed = true)
//    private val errorMapper: ErrorMapper = mockk()
//    private val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())
//    private var globalExceptionHandler = GlobalExceptionHandler(errorMapper)
//    private lateinit var userController: UserController
//
//    private var user = UserDataFactory.createUser(ID)
//    private var userRequestDto = UserDataFactory.createUserRequestDto()
//    private var userResponseDto = UserMapper().toUserResponseDto(user)
//    private var errorBadRequest = ErrorDataFactory.createBadRequestError()
//
//    @BeforeEach
//    fun setUp() {
//        userController = UserController(
//            userService,
//            reservationService,
//            userMapper,
//            journalMapper,
//            reservationMapper
//        )
//
//        mockMvc = MockMvcBuilders.standaloneSetup(userController)
//            .setControllerAdvice(globalExceptionHandler)
//            .build()
//    }
//
//    @Test
//    fun shouldCreateUser() {
//        //GIVEN
//        val expected = objectMapper.writeValueAsString(userResponseDto)
//        val content = objectMapper.writeValueAsString(userRequestDto)
//
//        every { userService.createUser(any()) } returns user
//        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isCreated)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithInvalidEmail() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(email = "first"))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithInvalidFirstName() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(firstName = "first"))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithInvalidLastName() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(lastName = "first"))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithInvalidPassword() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(password = "first"))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithInvalidPhone() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(phoneNumber = "123456789"))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithEmptyEmail() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(email = ""))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithEmptyFirstName() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(firstName = ""))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithEmptyLastName() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(lastName = ""))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithEmptyPassword() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(password = ""))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldNotCreateWithEmptyPhone() {
//        //GIVEN
//        val content = objectMapper.writeValueAsString(userRequestDto.copy(phoneNumber = ""))
//        val expected = objectMapper.writeValueAsString(errorBadRequest)
//
//        every { userService.createUser(any()) } throws IllegalArgumentException("Invalid")
//        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorBadRequest
//
//        //WHEN
//        val actual = mockMvc.perform(
//            post(URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldUpdateUser() {
//        //GIVEN
//        val expected = objectMapper.writeValueAsString(userResponseDto)
//        val content = objectMapper.writeValueAsString(userRequestDto)
//
//        every { userService.updateUser(any()) } returns user
//        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto
//
//        //WHEN
//        val actual = mockMvc.perform(
//            put("$URL/{id}", ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isOk)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldGetUserById() {
//        //GIVEN
//        val expected = objectMapper.writeValueAsString(userResponseDto)
//        val content = objectMapper.writeValueAsString(userRequestDto)
//
//        every { userService.getUserById(any()) } returns user
//        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto
//
//        //WHEN
//        val actual = mockMvc.perform(
//            put("$URL/{id}", ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isOk)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldGetReservationsByUserId() {
//        //GIVEN
//        val reservation = ReservationDataFactory.createReservation(ID)
//        val reservationDto = reservationMapper.toReservationDto(reservation)
//
//        val expected = objectMapper.writeValueAsString(listOf(reservationDto))
//        val content = objectMapper.writeValueAsString(userRequestDto)
//
//        every { reservationService.getAllReservationsByUserId(any()) } returns listOf(reservation)
//        every { reservationMapper.toReservationDto(any<List<Reservation>>()) } returns listOf(reservationDto)
//
//        val actual = mockMvc.perform(
//            get("$URL/{id}/reservations", ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isOk)
//            .andReturn().response.contentAsString
//
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldGetJournalsByUserId() {
//        //GIVEN
//        val journal = JournalDataFactory.createJournal(ID)
//        val journalDto = journalMapper.toJournalDto(journal)
//
//        val content = objectMapper.writeValueAsString(userRequestDto)
//        val expected = objectMapper.writeValueAsString(listOf(journalDto))
//
//        every { userService.findJournalsByUser(any()) } returns listOf(journal)
//        every { journalMapper.toJournalDto(any<List<Journal>>()) } returns listOf(journalDto)
//
//        //WHEN
//        val actual = mockMvc.perform(
//            get("$URL/{id}/journals", ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content)
//        )
//            .andExpect(status().isOk)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldGetUserByPhoneNumberOrEmail() {
//        //GIVEN
//        val expected = objectMapper.writeValueAsString(userResponseDto)
//
//        every { userService.getUserByPhoneNumberOrEmail(any(), any()) } returns user
//        every { userMapper.toUserResponseDto(any<User>()) } returns userResponseDto
//
//        //WHEN
//        val actual = mockMvc.perform(
//            get(URL)
//                .param("phoneNumber", "1234567890")
//                .param("email", "first@example.com")
//                .contentType(MediaType.APPLICATION_JSON)
//        )
//            .andExpect(status().isOk)
//            .andReturn().response.contentAsString
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldReserveBook() {
//        // GIVEN
//        val reservation = ReservationDataFactory.createReservation(ID)
//        val outcome = ReservationOutcome.Reservations(listOf(reservation))
//        val reservationDto = reservationMapper.toReservationDto(reservation)
//        val expected = objectMapper.writeValueAsString(listOf(reservationDto))
//
//        every { reservationService.reserveBook(
//            ID,
//            ID,
//            ID
//        ) } returns outcome
//
//        every { reservationService.getAllReservationsByUserId(ID) } returns listOf(reservation)
//        every { reservationMapper.toReservationDto(listOf(reservation)) } returns listOf(reservationDto)
//
//        // WHEN
//        val actual = mockMvc.perform(
//            post("$URL/{id}/reservations", ID)
//                .param("libraryId", ID)
//                .param("bookId", ID)
//        )
//            .andExpect(status().isCreated)
//            .andReturn().response.contentAsString
//
//        // THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldBorrowBookInsteadOfReservation() {
//        // GIVEN
//        val journal = JournalDataFactory.createJournal(ID)
//        val outcome = ReservationOutcome.Journals(listOf(journal))
//        val journalDto = journalMapper.toJournalDto(journal)
//        val expected = objectMapper.writeValueAsString(listOf(journalDto))
//
//        every { reservationService.reserveBook(
//            ID,
//            ID,
//            ID
//        ) } returns outcome
//
//        every { journalService.getJournalByUserId(ID) } returns listOf(journal)
//        every { journalMapper.toJournalDto(listOf(journal)) } returns listOf(journalDto)
//
//        // WHEN
//        val actual = mockMvc.perform(
//            post("$URL/{id}/reservations", ID)
//                .param("libraryId", ID)
//                .param("bookId", ID)
//        )
//            .andExpect(status().isCreated)
//            .andReturn().response.contentAsString
//
//        // THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    @Test
//    fun shouldCancelReservation() {
//        //GIVEN
//        val expected = 204
//
//        every { reservationService.cancelReservation(any(), any()) } returns Unit
//
//        //WHEN
//        val actual = mockMvc.perform(
//            delete("$URL/{id}/reservations", ID)
//                .param("id", ID)
//                .contentType(MediaType.APPLICATION_JSON)
//        )
//            .andExpect(status().isNoContent)
//            .andReturn().response.status
//
//        //THEN
//        Assertions.assertEquals(expected, actual)
//    }
//
//    companion object {
//        const val ID = UserDataFactory.JPA_ID.toString()
//        const val URL = "/api/v1/user"
//    }
//}
