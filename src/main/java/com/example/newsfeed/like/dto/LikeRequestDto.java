package com.example.newsfeed.like.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LikeRequestDto {
    @NotNull
    private Long id;
}
