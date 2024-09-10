package com.example.librarymanagement.controller

import com.example.librarymanagement.data.BookPresenceDataFactory
import com.example.librarymanagement.data.ErrorDataFactory
import com.example.librarymanagement.data.JournalDataFactory
import com.example.librarymanagement.dto.mapper.BookPresenceMapper
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.dto.mapper.JournalMapper
import com.example.librarymanagement.exception.EntityNotFoundException
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.domain.BookPresence
import com.example.librarymanagement.model.domain.Journal
import com.example.librarymanagement.service.BookPresenceService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class BookPresenceControllerTest {
    private lateinit var mockMvc: MockMvc
    private val bookPresenceService: BookPresenceService = mockk(relaxed = true)
    private val bookPresenceMapper: BookPresenceMapper = mockk(relaxed = true)
    private val errorMapper: ErrorMapper = mockk()
    private val globalExceptionHandler = GlobalExceptionHandler(errorMapper)

    private val journalMapper: JournalMapper = mockk(relaxed = true)
    private val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())
    private val bookPresenceController = BookPresenceController(bookPresenceService, bookPresenceMapper, journalMapper)

    private val bookPresence = BookPresenceDataFactory.createBookPresence(ID)
    private val bookPresenceDto = BookPresenceMapper().toBookPresenceDto(bookPresence)
    private val journal = JournalDataFactory.createJournal(ID)
    private val journalDto = journalMapper.toJournalDto(journal)
    private val errorNotFound = ErrorDataFactory.createNotFoundError()

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookPresenceController)
            .setControllerAdvice(globalExceptionHandler)
            .build()
    }

    @Test
    fun shouldAddUserToBook() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(journalDto))

        every { bookPresenceService.addUserToBook(any(), any(), any()) } returns listOf(journal)
        every { journalMapper.toJournalDto(any<List<Journal>>()) } returns listOf(journalDto)

        //WHEN
        val actual = mockMvc.perform(
            post("$USER_URL{ID}/borrowings", ID)
                .param("libraryId", ID)
                .param("bookId", ID)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotAddUserToBookWithNonExistingUser() {
        //GIVEN
        val expected = 404

        every { bookPresenceService.addUserToBook(any(), any(), any()) } throws EntityNotFoundException("User")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorNotFound

        //WHEN
        val actual = mockMvc.perform(
            post("$USER_URL{ID}/borrowings", 0L)
                .param("libraryId", ID)
                .param("bookId", ID)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andReturn().response.status

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotAddUserToBookWithNonExistingLibrary() {
        //GIVEN
        val expected = 404

        every { bookPresenceService.addUserToBook(any(), any(), any()) } throws EntityNotFoundException("Library")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorNotFound

        //WHEN
        val actual = mockMvc.perform(
            post("$USER_URL{ID}/borrowings", ID)
                .param("libraryId", "0")
                .param("bookId", ID)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andReturn().response.status

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotAddUserToBookWithNonExistingBook() {
        //GIVEN
        val expected = 404
        every { bookPresenceService.addUserToBook(any(), any(), any()) } throws EntityNotFoundException("Book")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorNotFound

        //WHEN
        val actual = mockMvc.perform(
            post("$USER_URL{ID}/borrowings", ID)
                .param("libraryId", ID)
                .param("bookId", "0")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andReturn().response.status

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldReturnBookToLibrary() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(journalDto))

        every { bookPresenceService.removeUserFromBook(any(), any(), any()) } returns listOf(journal)
        every { journalMapper.toJournalDto(any<List<Journal>>()) } returns listOf(journalDto)

        //WHEN
        val actual = mockMvc.perform(
            delete("$USER_URL{ID}/borrowings", ID)
                .param("libraryId", ID)
                .param("bookId", ID)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent)
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotReturnBookToLibraryWithNonExistingUser() {
        //GIVEN
        val expected = 404

        every { bookPresenceService.removeUserFromBook(any(), any(), any()) } throws EntityNotFoundException("User")
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorNotFound

        //WHEN
        val actual = mockMvc.perform(
            delete("$USER_URL{ID}/borrowings", 0L)
                .param("libraryId", ID)
                .param("bookId", ID)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andReturn().response.status

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldAddBookToLibrary() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(bookPresenceDto)

        every { bookPresenceService.addBookToLibrary(any(), any()) } returns bookPresence
        every { bookPresenceMapper.toBookPresenceDto(any<BookPresence>()) } returns bookPresenceDto

        //WHEN
        val actual = mockMvc.perform(
            post("$LIBRARY_URL{libraryId}/book/{bookId}/presence", ID, ID)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllBooksByLibraryId() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllByLibraryId(any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        //WHEN
        val actual = mockMvc.perform(
            get("$LIBRARY_URL{libraryId}/book", ID)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllBooksByLibraryIdAndBookId() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllBookPresencesByLibraryIdAndBookId(any(), any()) } returns listOf(bookPresence)
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        //WHEN
        val actual = mockMvc.perform(
            get("$LIBRARY_URL{libraryId}/book/{bookId}/presence", ID, ID)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllBooksByLibraryIdAndAvailability1() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllBookPresencesByLibraryIdAndAvailability(any(), any()) } returns listOf(
            bookPresence
        )
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        //WHEN
        val actual = mockMvc.perform(
            get("$LIBRARY_URL{libraryId}/book", ID)
                .param("availability", "AVAILABLE")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldGetAllBooksByLibraryIdAndAvailability2() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(listOf(bookPresenceDto))

        every { bookPresenceService.getAllBookPresencesByLibraryIdAndAvailability(any(), any()) } returns listOf(
            bookPresence
        )
        every { bookPresenceMapper.toBookPresenceDto(any<List<BookPresence>>()) } returns listOf(bookPresenceDto)

        //WHEN
        val actual = mockMvc.perform(
            get("$LIBRARY_URL{libraryId}/book", ID)
                .param("availability", "UNAVAILABLE")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldRemoveBookFromLibrary() {
        //GIVEN
        val status = 204

        every { bookPresenceService.deleteBookPresenceById(any()) } returns Unit

        //WHEN
        val actual = mockMvc.perform(
            delete(LIBRARY_URL + "presence/{presenceId}", ID)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent())
            .andReturn().response.status

        //THEN
        Assertions.assertEquals(status, actual)
    }

    companion object {
        const val ID = BookPresenceDataFactory.JPA_ID.toString()
        const val LIBRARY_URL = "/api/v1/library/"
        const val USER_URL = "/api/v1/user/"
    }
}
