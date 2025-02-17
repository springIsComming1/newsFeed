package com.example.newsfeed.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotNull
    private Long postId;

    @NotBlank
    private String contents;
}
