package com.example.newsfeed.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostSaveRequestDto {

    @NotBlank(message = "제목은 공백으로 설정할 수 업습니다.")
    private String title;

    @NotBlank(message = "내용은 공백으로 설정할 수 업습니다.")
    private String content;
}
