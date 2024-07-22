package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.UserRequestDto;
import com.example.librarymanagement.dto.UserResponseDto;
import com.example.librarymanagement.model.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User toUser(UserRequestDto userRequestDto) {
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        String hashedPassword = hashPassword(userRequestDto.getPlainPassword());
        user.setPassword(hashedPassword);
        return user;
    }

    private static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainPassword, salt);
    }

    public static List<UserResponseDto> toUsers(List<User> usersList) {
        List<UserResponseDto> users = new ArrayList<>();

        if(!usersList.isEmpty()){
            users = usersList.stream().map(UserResponseDto::new).toList();
        }
        return users;
    }
}