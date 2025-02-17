package com.example.newsfeed.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentFindAllResponseDto {

    private Long Id;
    private String contents;
    private Long boardId;
}
