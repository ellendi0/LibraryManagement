package com.example.librarymanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.librarymanagement.model.entity.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    List<ReservationDto> reservations;

    public UserResponseDto(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        phoneNumber = user.getPhoneNumber();
    }
}