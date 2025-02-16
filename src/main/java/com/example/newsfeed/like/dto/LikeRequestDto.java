package com.example.newsfeed.like.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LikeRequestDto {
    @NotBlank
    private Long id;
}
