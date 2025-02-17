package com.example.newsfeed.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardLikeRequestDto {
    @NotBlank(message = "좋아요 누르려는 게시물의 번호를 입력하세요")
    private Long id;
}
