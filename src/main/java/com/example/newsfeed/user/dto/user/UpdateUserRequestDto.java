package com.example.newsfeed.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserRequestDto {

    private String name; // null 허용 → 개별 필드 업데이트 가능

    @Email(message = "이메일의 형식이 올바르지 않습니다.")
    private String email; // 이메일 형식 검증 유지

    public UpdateUserRequestDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}