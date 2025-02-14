package com.example.newsfeed.user.controller;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.user.dto.LoginRequestDto;
import com.example.newsfeed.user.dto.LoginResponseDto;
import com.example.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        LoginResponseDto responseDto = userService.login(requestDto.getEmail(), requestDto.getPassword());
        HttpSession session = request.getSession();
        session.setAttribute(Const.LOGIN_USER, requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
