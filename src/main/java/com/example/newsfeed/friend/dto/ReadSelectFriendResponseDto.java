package com.example.newsfeed.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadSelectFriendResponseDto {

    private final String email;

    private final String name;
}
