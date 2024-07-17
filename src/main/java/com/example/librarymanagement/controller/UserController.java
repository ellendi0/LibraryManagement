package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.JournalDto;
import com.example.librarymanagement.dto.ReservationDto;
import com.example.librarymanagement.dto.UserRequestDto;
import com.example.librarymanagement.dto.UserResponseDto;
import com.example.librarymanagement.dto.mapper.JournalMapper;
import com.example.librarymanagement.dto.mapper.ReservationMapper;
import com.example.librarymanagement.dto.mapper.UserMapper;
import com.example.librarymanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JournalMapper journalMapper;
    private final ReservationMapper reservationMapper;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto findById(@PathVariable Long id) {
        return userMapper.toUserResponseDto(userService.getUserById(id));
    }

    @GetMapping(params = {"email", "phoneNumber"})
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserByPhoneNumberOrEmail(@RequestParam(name = "email", required = false) String email,
                                                       @RequestParam(name = "phoneNumber", required = false) String phoneNumber) {
        return userMapper.toUserResponseDto(userService.getUserByPhoneNumberOrEmail(email, phoneNumber));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto user) {
        return userMapper.toUserResponseDto(userService.createUser(userMapper.toUser(user)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @RequestBody @Valid UserRequestDto userRequestDto) {
        return userMapper.toUserResponseDto(userService.updateUser(id, userMapper.toUser(userRequestDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}/reservations")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationDto> findReservationsByUser(@PathVariable Long id) {
        return reservationMapper.toReservationDto(userService.findReservationsByUser(id));
    }

    @GetMapping("/{id}/journals")
    @ResponseStatus(HttpStatus.OK)
    public List<JournalDto> findJournalsByUser(@PathVariable Long id) {
        return journalMapper.toJournalDto(userService.findJournalsByUser(id));
    }

    @PostMapping("/{id}/borrowings")
    @ResponseStatus(HttpStatus.CREATED)
    public List<JournalDto> borrowBookFromLibrary(@PathVariable(name = "id") Long userId,
                                                                  @RequestParam Long libraryId,
                                                                  @RequestParam Long bookId) {
        return journalMapper.toJournalDto(userService.borrowBookFromLibrary(userId, libraryId, bookId));
    }

    @PostMapping("/{id}/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ReservationDto> reserveBookInLibrary(@PathVariable(name = "id") Long userId,
                                                                       @RequestParam(required = false) Long libraryId,
                                                                       @RequestParam Long bookId) {
        return reservationMapper.toReservationDto(userService.reserveBookInLibrary(userId, libraryId, bookId));
    }

    @DeleteMapping("/{id}/borrowings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public List<JournalDto> returnBookToLibrary(@PathVariable(name = "id") Long userId,
                                                             @RequestParam Long libraryId,
                                                             @RequestParam Long bookId) {
        return journalMapper.toJournalDto(userService.returnBookToLibrary(userId, libraryId, bookId));
    }

    @DeleteMapping("/{userId}/reservations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelReservationInLibrary(@PathVariable Long userId,
                                           @RequestParam(name = "id") Long reservationId) {
        userService.cancelReservationInLibrary(userId, reservationId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllUsers() {
        return userMapper.toUserResponseDto(userService.findAll());
    }
}