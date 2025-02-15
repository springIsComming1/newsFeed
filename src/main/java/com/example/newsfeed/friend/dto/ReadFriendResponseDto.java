package com.example.newsfeed.friend.dto;

import com.example.newsfeed.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadFriendResponseDto {

    private final Long id;

    private final String name;

    private final String email;
}
