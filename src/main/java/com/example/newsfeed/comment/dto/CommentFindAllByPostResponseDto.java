package com.example.newsfeed.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentFindAllByPostResponseDto {

    private Long Id;
    private String contents;
}
