package com.example.newsfeed.friend.dto;

import com.example.newsfeed.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReadFriendPostResponseDto {

    private final String title;

    private final String content;

    private final String useranme;

    private final LocalDateTime modifiedAt;
}
