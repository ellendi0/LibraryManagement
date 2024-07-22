package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserRequestDto {
    @Size(max = 50, message = "First name must contain no more than 50 characters")
    @Pattern(regexp = "[A-Z][a-z]+",
            message = "First name must start with a capital letter followed by one or more lowercase letters")
    @NotBlank(message = "First name can't be empty")
    private String firstName;

    @Size(max = 50, message = "Last name must contain no more than 50 characters")
    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Last name must start with a capital letter followed by one or more lowercase letters")
    @NotBlank(message = "Last name can't be empty")
    private String lastName;

    @Email(message = "Invalid e-mail address")
    @NotBlank(message = "Email can't be empty")
    private String email;

    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}",
            message = "Password must be at least 6 characters, one uppercase letter and one number")
    @NotBlank(message = "Password can't be empty")
    private String plainPassword;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    public UserRequestDto(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        plainPassword = user.getPassword();
        phoneNumber = user.getPhoneNumber();
    }

    @JsonProperty("password")
    public String getPlainPassword() {
        return plainPassword;
    }
}