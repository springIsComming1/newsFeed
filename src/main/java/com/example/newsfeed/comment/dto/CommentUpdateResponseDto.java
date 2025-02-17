package com.example.newsfeed.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentUpdateResponseDto {

    private Long commentId;
    private Long postId;
    private String contents;
}
