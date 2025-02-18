package com.example.newsfeed.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserRequestDto {

    @NotBlank(message = "이름은 비워둘 수 없습니다.")
    private String name;

    @NotBlank(message = "이메일은 비워둘 수 없습니다.")
    @Email(message = "이메일의 형식에 어긋납니다.")
    private String email;

    public UpdateUserRequestDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}