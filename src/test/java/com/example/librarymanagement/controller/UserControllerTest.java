package com.example.librarymanagement.controller;

import com.example.librarymanagement.data.TestDataFactory;
import com.example.librarymanagement.dto.ErrorDto;
import com.example.librarymanagement.dto.JournalDto;
import com.example.librarymanagement.dto.ReservationDto;
import com.example.librarymanagement.dto.UserRequestDto;
import com.example.librarymanagement.dto.UserResponseDto;
import com.example.librarymanagement.dto.mapper.ErrorMapper;
import com.example.librarymanagement.dto.mapper.JournalMapper;
import com.example.librarymanagement.dto.mapper.ReservationMapper;
import com.example.librarymanagement.dto.mapper.UserMapper;
import com.example.librarymanagement.exception.EntityNotFoundException;
import com.example.librarymanagement.exception.GlobalExceptionHandler;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.model.entity.Reservation;
import com.example.librarymanagement.model.entity.User;
import com.example.librarymanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private ErrorMapper errorMapper;

    @MockBean
    private ReservationMapper reservationMapper;

    @MockBean
    private JournalMapper journalMapper;

    @MockBean
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDto userRequestDto;
    private ReservationDto reservationDto;
    private JournalDto journalDto;
    private UserResponseDto userResponseDto;
    private static User user1;
    private static Journal journal;
    private static Reservation reservation;
    private static ErrorDto errorDto1;
    private static ErrorDto errorDto2;

    @BeforeEach
    void setUp() {
        ReservationMapper reservationMapper1 = new ReservationMapper();
        JournalMapper journalMapper1 = new JournalMapper();
        UserMapper userMapper1 = new UserMapper();
        ErrorMapper errorMapper1 = new ErrorMapper();

        TestDataFactory.TestDataRel testData = TestDataFactory.createTestDataRel();

        user1 = testData.user;
        journal = testData.journal;
        reservation = testData.reservation;

        userRequestDto = TestDataFactory.createUserRequestDto();
        userResponseDto = userMapper1.toUserResponseDto(user1);
        reservationDto = reservationMapper1.toReservationDto(reservation);
        journalDto = journalMapper1.toJournalDto(journal);
        errorDto1 = errorMapper1.toErrorDto(HttpStatus.BAD_REQUEST, "Invalid data");
        errorDto2 = errorMapper1.toErrorDto(HttpStatus.NOT_FOUND, "User");
    }


    @Test
    void createUser() throws Exception {
        String expected = objectMapper.writeValueAsString(userResponseDto);

        given(userService.createUser(any(User.class))).willReturn(user1);
        given(userMapper.toUserResponseDto((User) any())).willReturn(userResponseDto);

        String result = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void createUserWithInvalidEmail() throws Exception {
        userRequestDto.setEmail("first.first.example.com");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithInvalidPassword() throws Exception {
        userRequestDto.setPassword("password");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithInvalidPhoneNumber() throws Exception {
        userRequestDto.setPhoneNumber("123456789");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithInvalidFirstName() throws Exception {
        userRequestDto.setFirstName("first");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithInvalidLastName() throws Exception {
        userRequestDto.setLastName("first");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithEmptyFirstName() throws Exception {
        userRequestDto.setFirstName("");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithEmptyLastName() throws Exception {
        userRequestDto.setLastName("");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithEmptyEmail() throws Exception {
        userRequestDto.setEmail("");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithEmptyPhoneNumber() throws Exception {
        userRequestDto.setPhoneNumber("");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserWithEmptyPassword() throws Exception {
        userRequestDto.setPassword("");

        given(userService.createUser(any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser() throws Exception {
        String expected = objectMapper.writeValueAsString(userResponseDto);

        given(userService.updateUser(anyLong(), any(User.class))).willReturn(user1);
        given(userMapper.toUserResponseDto((User) any())).willReturn(userResponseDto);

        String result = mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void updateUserWithInvalidEmail() throws Exception {
        userRequestDto.setEmail("second.second.example.com");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithInvalidPassword() throws Exception {
        userRequestDto.setPassword("newpassword");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithInvalidPhoneNumber() throws Exception {
        userRequestDto.setPhoneNumber("098765432");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithInvalidFirstName() throws Exception {
        userRequestDto.setFirstName("second");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithInvalidLastName() throws Exception {
        userRequestDto.setLastName("second");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithEmptyFirstName() throws Exception {
        userRequestDto.setFirstName("");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithEmptyLastName() throws Exception {
        userRequestDto.setLastName("");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithEmptyEmail() throws Exception {
        userRequestDto.setEmail("");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithEmptyPhoneNumber() throws Exception {
        userRequestDto.setPhoneNumber("");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWithEmptyPassword() throws Exception {
        userRequestDto.setPassword("");

        given(userService.updateUser(anyLong(), any(User.class))).willThrow(new IllegalArgumentException());
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto1);
        given(globalExceptionHandler.handleIllegalArgumentException(any(IllegalArgumentException.class))).willReturn(errorDto1);

        mockMvc.perform(put("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser() throws Exception {
        willDoNothing().given(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/v1/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void findById() throws Exception {
        String expected = objectMapper.writeValueAsString(userResponseDto);

        given(userService.getUserById(anyLong())).willReturn(user1);
        given(userMapper.toUserResponseDto((User) any())).willReturn(userResponseDto);

        String result = mockMvc.perform(get("/api/v1/user/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void findByNonExistingId() throws Exception {
        given(userService.getUserById(anyLong())).willThrow(new EntityNotFoundException("User"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
        given(globalExceptionHandler.handleEntityNotFoundException(any(EntityNotFoundException.class))).willReturn(errorDto2);

        mockMvc.perform(get("/api/v1/user/{id}", 0L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findReservationsByUser() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(reservationDto));

        given(userService.findReservationsByUser(anyLong())).willReturn(Collections.singletonList(reservation));
        given(reservationMapper.toReservationDto(List.of(reservation))).willReturn(List.of(reservationDto));

        String result = mockMvc.perform(get("/api/v1/user/{id}/reservations", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void findReservationsByNonExistingUser() throws Exception {
        given(userService.findReservationsByUser(anyLong())).willThrow(new EntityNotFoundException("User"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
        given(globalExceptionHandler.handleEntityNotFoundException(any(EntityNotFoundException.class))).willReturn(errorDto2);

        mockMvc.perform(get("/api/v1/user/{id}/reservations", 0L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findJournalsByUser() throws Exception {
        String expected = objectMapper.writeValueAsString(Collections.singletonList(journalDto));

        given(userService.findJournalsByUser(anyLong())).willReturn(Collections.singletonList(journal));
        given(journalMapper.toJournalDto(List.of(journal))).willReturn(List.of(journalDto));

        String result = mockMvc.perform(get("/api/v1/user/{id}/journals", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }

    @Test
    void getUserByPhoneNumberOrEmail() throws Exception {
        String expected = objectMapper.writeValueAsString(userResponseDto);

        given(userService.getUserByPhoneNumberOrEmail(any(String.class), any(String.class))).willReturn(user1);
        given(userMapper.toUserResponseDto((User) any())).willReturn(userResponseDto);
        given(userService.getUserByPhoneNumberOrEmail(any(String.class), any(String.class))).willReturn(user1);

        String result = mockMvc.perform(get("/api/v1/user")
                        .param("email", "first@example.com")
                        .param("phoneNumber", "1234567890")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);

    }

    @Test
    void getUserByPhoneNumberOrEmailWithNonExistingUser() throws Exception {
        given(userService.getUserByPhoneNumberOrEmail(any(String.class), any(String.class))).willThrow(new EntityNotFoundException("User"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
        given(globalExceptionHandler.handleEntityNotFoundException(any(EntityNotFoundException.class))).willReturn(errorDto2);

        mockMvc.perform(get("/api/v1/user")
                        .param("email", "first@example.com")
                        .param("phoneNumber", "1234567890")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void borrowBookFromLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(journalDto));

        given(userService.borrowBookFromLibrary(anyLong(), anyLong(), anyLong())).willReturn(List.of(journal));
        given(journalMapper.toJournalDto(List.of(journal))).willReturn(List.of(journalDto));

        String result = mockMvc.perform(post("/api/v1/user/{id}/borrowings", 1L)
                        .param("libraryId", "1")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);

    }

    @Test
    void borrowBookFromLibraryWithNonExistingUser() throws Exception {
        given(userService.borrowBookFromLibrary(anyLong(), anyLong(), anyLong())).willThrow(new EntityNotFoundException("User"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
        given(globalExceptionHandler.handleEntityNotFoundException(any(EntityNotFoundException.class))).willReturn(errorDto2);

        mockMvc.perform(post("/api/v1/user/{id}/borrowings", 0L)
                        .param("libraryId", "1")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void borrowBookFromLibraryWithNonExistingLibrary() throws Exception {
        given(userService.borrowBookFromLibrary(anyLong(), anyLong(), anyLong())).willThrow(new EntityNotFoundException("Library"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
        given(globalExceptionHandler.handleEntityNotFoundException(any(EntityNotFoundException.class))).willReturn(errorDto2);

        mockMvc.perform(post("/api/v1/user/{id}/borrowings", 1L)
                        .param("libraryId", "0")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void borrowBookFromLibraryWithNonExistingBook() throws Exception {
        given(userService.borrowBookFromLibrary(anyLong(), anyLong(), anyLong())).willThrow(new EntityNotFoundException("Book"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
        given(globalExceptionHandler.handleEntityNotFoundException(any(EntityNotFoundException.class))).willReturn(errorDto2);

        mockMvc.perform(post("/api/v1/user/{id}/borrowings", 1L)
                        .param("libraryId", "1")
                        .param("bookId", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void reserveBookInLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(reservationDto));

        given(userService.reserveBookInLibrary(anyLong(), anyLong(), anyLong())).willReturn(List.of(reservation));
        given(reservationMapper.toReservationDto(List.of(reservation))).willReturn(List.of(reservationDto));

        String result = mockMvc.perform(post("/api/v1/user/{id}/reservations", 1L)
                        .param("libraryId", "1")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);

    }

    @Test
    void reserveBookInLibraryWithNonExistingUser() throws Exception {
        given(userService.reserveBookInLibrary(anyLong(), anyLong(), anyLong())).willThrow(new EntityNotFoundException("User"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
        given(globalExceptionHandler.handleEntityNotFoundException(any(EntityNotFoundException.class))).willReturn(errorDto2);

        mockMvc.perform(post("/api/v1/user/{id}/reservations", 0L)
                        .param("libraryId", "1")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void reserveBookInLibraryWithNonExistingLibrary() throws Exception {
        given(userService.reserveBookInLibrary(anyLong(), anyLong(), anyLong())).willThrow(new EntityNotFoundException("Library"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
        given(globalExceptionHandler.handleEntityNotFoundException(any(EntityNotFoundException.class))).willReturn(errorDto2);

        mockMvc.perform(post("/api/v1/user/{id}/reservations", 1L)
                        .param("libraryId", "0")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void reserveBookInLibraryWithNonExistingBook() throws Exception {
        given(userService.reserveBookInLibrary(anyLong(), anyLong(), anyLong())).willThrow(new EntityNotFoundException("Book"));
        given(errorMapper.toErrorDto((HttpStatus) any(), (List<String>) any())).willReturn(errorDto2);
        given(globalExceptionHandler.handleEntityNotFoundException(any(EntityNotFoundException.class))).willReturn(errorDto2);

        mockMvc.perform(post("/api/v1/user/{id}/reservations", 1L)
                        .param("libraryId", "1")
                        .param("bookId", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancelReservationInLibrary() throws Exception {
        willDoNothing().given(userService).cancelReservationInLibrary(anyLong(), anyLong());

        mockMvc.perform(delete("/api/v1/user/{id}/reservations", 1L)
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void returnBookToLibrary() throws Exception {
        String expected = objectMapper.writeValueAsString(List.of(journalDto));

        given(userService.returnBookToLibrary(anyLong(), anyLong(), anyLong())).willReturn(List.of(journal));
        given(journalMapper.toJournalDto(List.of(journal))).willReturn(List.of(journalDto));

        String result = mockMvc.perform(delete("/api/v1/user/{id}/borrowings", 1L)
                        .param("libraryId", "1")
                        .param("bookId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, result);
    }
}