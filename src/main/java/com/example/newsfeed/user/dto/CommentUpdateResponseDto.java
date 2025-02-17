package com.example.newsfeed.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentUpdateResponseDto {

    private Long commentId;
    private Long boardId;
    private String contents;
}
