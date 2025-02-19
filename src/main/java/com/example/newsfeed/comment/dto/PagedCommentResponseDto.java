package com.example.newsfeed.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedCommentResponseDto {
    private List<CommentFindAllByPostResponseDto> comments;
    private int pageNumber;
    private int totalPage;
}