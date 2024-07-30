package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.UserRequestDto;
import com.example.librarymanagement.dto.UserResponseDto;
import com.example.librarymanagement.model.entity.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UserMapper {
    private static final String salt = BCrypt.gensalt(12);

    public User toUser(UserRequestDto userRequestDto) {
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setPassword(hashPassword(userRequestDto.getPassword()));
        return user;
    }

    public UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        return userResponseDto;
    }

    public List<UserResponseDto> toUserResponseDto(List<User> users) {
        if(CollectionUtils.isEmpty(users)) return Collections.emptyList();

        return users.stream()
                .map(this::toUserResponseDto)
                .toList();
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, salt);
    }
}