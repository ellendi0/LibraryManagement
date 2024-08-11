package com.example.librarymanagement.controller

import com.example.librarymanagement.data.ErrorDataFactory
import com.example.librarymanagement.data.LibraryDataFactory
import com.example.librarymanagement.dto.mapper.ErrorMapper
import com.example.librarymanagement.dto.mapper.LibraryMapper
import com.example.librarymanagement.exception.GlobalExceptionHandler
import com.example.librarymanagement.model.entity.Library
import com.example.librarymanagement.service.LibraryService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class LibraryControllerTest {

    private lateinit var libraryService: LibraryService
    private lateinit var libraryMapper: LibraryMapper
    private lateinit var libraryController: LibraryController
    private lateinit var mockMvc: MockMvc
    private lateinit var errorMapper: ErrorMapper
    private lateinit var globalExceptionHandler: GlobalExceptionHandler
    private val objectMapper: ObjectMapper = ObjectMapper()

    private var library = LibraryDataFactory.createLibrary()
    private var libraryDto = LibraryMapper().toLibraryDto(library)
    private var errorDtoBadRequest = ErrorDataFactory.createBadRequestError()

    @BeforeEach
    fun setUp() {
        libraryService = mockk(relaxed = true)
        libraryMapper = mockk(relaxed = true)
        errorMapper = mockk(relaxed = true)
        globalExceptionHandler = GlobalExceptionHandler(errorMapper)
        libraryController = LibraryController(libraryService, libraryMapper)

        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).setControllerAdvice(globalExceptionHandler).build()
    }

    @Test
    fun shouldCreateLibrary() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(libraryDto)

        every { libraryService.createLibrary(any()) } returns library
        every { libraryMapper.toLibraryDto(any<Library>()) } returns libraryDto

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/library")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(libraryDto)))
            .andExpect(status().isCreated())
            .andReturn().response.contentAsString;

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotCreateLibraryWithInvalidData() {
        //GIVEN
        val content = objectMapper.writeValueAsString(libraryDto.copy(name = ""))
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)

        every { libraryService.createLibrary(any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(post("/api/v1/library")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldUpdateLibrary() {
        //GIVEN
        val expected = objectMapper.writeValueAsString(libraryDto)
        val content = objectMapper.writeValueAsString(libraryDto)

        every { libraryService.updateLibrary(any()) } returns library
        every { libraryMapper.toLibraryDto(any<Library>()) } returns libraryDto

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/library/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString;

        //GIVEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldNotUpdateLibraryWithInvalidData() {
        //GIVEN
        val content = objectMapper.writeValueAsString(libraryDto.copy(name = ""))
        val expected = objectMapper.writeValueAsString(errorDtoBadRequest)

        every { libraryService.updateLibrary(any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDtoBadRequest

        //WHEN
        val actual = mockMvc.perform(put("/api/v1/library/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isBadRequest())
            .andReturn().response.contentAsString

        //THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun shouldDeleteLibrary() {
        //GIVEN
        val expected = 204

        every { libraryService.deleteLibrary(any()) } returns Unit
        every { libraryMapper.toLibraryDto(any<Library>()) } returns libraryDto

        //WHEN
        val actual = mockMvc.perform(delete("/api/v1/library/{id}", 1L))
            .andExpect(status().isNoContent())
            .andReturn().response.status

        //THEN
        Assertions.assertEquals(expected, actual)
    }
}
