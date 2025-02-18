package com.example.newsfeed.user.dto.user;

import com.example.newsfeed.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;

    //친구가 아닐 경우 (id, email만 반환)
    public UserResponseDto(Long id) {
        this.id = id;
    }

    //친구일 경우 (id, name, email 모두 반환)
    public UserResponseDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static UserResponseDto toDtoNonFriend(User user) {
        return new UserResponseDto(user.getId());
    }

    public static UserResponseDto toDtoFriend(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }
}