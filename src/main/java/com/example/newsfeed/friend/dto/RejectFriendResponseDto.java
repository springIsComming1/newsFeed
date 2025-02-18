package com.example.newsfeed.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RejectFriendResponseDto {

    private final String receiverName;

    private final String requesterName;

    private final String status;
}
