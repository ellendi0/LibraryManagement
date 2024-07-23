package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.JournalDto;
import com.example.librarymanagement.dto.ReservationDto;
import com.example.librarymanagement.dto.UserRequestDto;
import com.example.librarymanagement.dto.UserResponseDto;
import com.example.librarymanagement.dto.mapper.JournalMapper;
import com.example.librarymanagement.dto.mapper.ReservationMapper;
import com.example.librarymanagement.dto.mapper.UserMapper;
import com.example.librarymanagement.model.entity.Journal;
import com.example.librarymanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(new UserResponseDto(userService.getUserById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUserByPhoneNumberOrEmail(@RequestParam(name = "email", required = false) String email,
                                                                       @RequestParam(name = "phoneNumber", required = false) String phoneNumber) {
        return new ResponseEntity<>(new UserResponseDto(userService.getUserByPhoneNumberOrEmail(email, phoneNumber)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto user) {
        return new ResponseEntity<>(new UserResponseDto(
                userService.createUser(UserMapper.toUser(user))), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
                                                      @RequestBody @Valid UserRequestDto userRequestDto) {
        return new ResponseEntity<>(
                new UserResponseDto(userService.updateUser(id, UserMapper.toUser(userRequestDto))), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationDto>> findReservationsByUser(@PathVariable Long id) {
        return new ResponseEntity<>(
                ReservationMapper.toReservationDto(userService.findReservationsByUser(id)), HttpStatus.OK);
    }

    @GetMapping("/{id}/journals")
    public ResponseEntity<List<JournalDto>> findJournalsByUser(@PathVariable Long id) {
        return new ResponseEntity<>(JournalMapper.toJournalDto(userService.findJournalsByUser(id)), HttpStatus.OK);
    }

    @PostMapping("/{id}/borrowings")
    public ResponseEntity<List<JournalDto>> borrowBookFromLibrary(@PathVariable(name = "id") Long userId,
                                                                  @RequestParam Long libraryId,
                                                                  @RequestParam Long bookId) {
        return new ResponseEntity<>(
                JournalMapper.toJournalDto(userService.borrowBookFromLibrary(userId, libraryId, bookId)), HttpStatus.OK);
    }

    @PostMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationDto>> reserveBookFromLibrary(@PathVariable(name = "id") Long userId,
                                                                       @RequestParam(required = false) Long libraryId,
                                                                       @RequestParam Long bookId) {
        return new ResponseEntity<>(
                ReservationMapper.toReservationDto(userService.reserveBookInLibrary(userId, libraryId, bookId)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}/borrowings")
    public ResponseEntity<List<JournalDto>> returnBookToLibrary(@PathVariable(name = "id") Long userId,
                                                             @RequestParam Long libraryId,
                                                             @RequestParam Long bookId) {
        return new ResponseEntity<>(JournalMapper.toJournalDto(userService.returnBookToLibrary(userId, libraryId, bookId)), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/reservations")
    public ResponseEntity<Void> cancelBookFromLibrary(@PathVariable(name = "id") Long userId,
                                                      @RequestParam Long bookId) {
        userService.cancelReservationInLibrary(userId, bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll().stream().map(UserResponseDto::new).toList(), HttpStatus.OK);
    }
}