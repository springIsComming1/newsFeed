package com.example.newsfeed.friend.dto;

import com.example.newsfeed.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApproveFriendResponseDto {

    private final String receiverName;

    private final String requesterName;

    private final String status;
}
