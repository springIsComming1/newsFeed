package com.example.newsfeed.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserRequestDto {

    @Size(min = 2, max = 18)
    private String name;


    @Email(message = "이메일의 형식에 어긋납니다.")
    private String email;

    public UpdateUserRequestDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}