package com.example.newsfeed.friend.dto;

import com.example.newsfeed.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveFriendsRequestResponseDto {

    private final Long id;

    private final String requesterName;

    private final String receiverName;

    private final String status;

}
