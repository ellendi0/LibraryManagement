package com.example.librarymanagement.controller

import com.example.librarymanagement.data.TestDataFactory
import com.example.librarymanagement.dto.ErrorDto
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
import org.springframework.http.HttpStatus
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

    private var library = TestDataFactory.createLibrary()
    private var libraryDto = LibraryMapper().toLibraryDto(library)
    private var errorDto = ErrorDto(HttpStatus.BAD_REQUEST.value(), listOf("Invalid"))

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
        val expected = objectMapper.writeValueAsString(libraryDto)

        every { libraryService.createLibrary(any()) } returns library
        every { libraryMapper.toLibraryDto(any<Library>()) } returns libraryDto

        val result = mockMvc.perform(post("/api/v1/library")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(libraryDto)))
            .andExpect(status().isCreated())
            .andReturn().response.contentAsString;

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotCreateLibraryWithInvalidData() {
        every { libraryService.createLibrary(any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto

        mockMvc.perform(post("/api/v1/library")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(libraryDto)))
            .andExpect(status().isBadRequest())
    }

    @Test
    fun shouldUpdateLibrary() {
        val expected = objectMapper.writeValueAsString(libraryDto)

        every { libraryService.updateLibrary(any(), any()) } returns library
        every { libraryMapper.toLibraryDto(any<Library>()) } returns libraryDto

        val result = mockMvc.perform(put("/api/v1/library/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(libraryDto)))
            .andExpect(status().isOk())
            .andReturn().response.contentAsString;

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun shouldNotUpdateLibraryWithInvalidData() {
        every { libraryService.updateLibrary(any(), any()) } throws (IllegalArgumentException("Invalid"))
        every { errorMapper.toErrorDto(any(), any<List<String>>()) } returns errorDto

        mockMvc.perform(put("/api/v1/library/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(libraryDto)))
            .andExpect(status().isBadRequest())
    }

    @Test
    fun shouldDeleteLibrary() {
        every { libraryService.deleteLibrary(any()) } returns Unit
        every { libraryMapper.toLibraryDto(any<Library>()) } returns libraryDto

        mockMvc.perform(delete("/api/v1/library/{id}", 1L))
            .andExpect(status().isNoContent())
    }
}
