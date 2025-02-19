package com.example.newsfeed.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentFindAllMineResponseDto {

    private Long Id;
    private String contents;
    private Long postId;
}