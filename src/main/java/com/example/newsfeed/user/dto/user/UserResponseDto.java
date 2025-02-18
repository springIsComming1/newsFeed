package com.example.newsfeed.user.dto.user;

import com.example.newsfeed.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;

    public UserResponseDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UserResponseDto(String email) {
        this.email = email;
    }

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(user.getEmail());
    }
}