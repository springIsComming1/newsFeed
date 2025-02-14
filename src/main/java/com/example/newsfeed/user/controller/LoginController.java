package com.example.newsfeed.user.controller;

import com.example.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logins")
public class LoginController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<LoginResponseDto> login(
            @Valid @ModelAttribute LoginRequestDto requestDto,
            HttpServletRequest request
    ){
        userService.login
    }
}
