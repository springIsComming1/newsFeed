package com.example.newsfeed.user.controller;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.user.dto.DeleteUserRequestDto;
import com.example.newsfeed.user.dto.UserRequestDto;
import com.example.newsfeed.user.dto.UserResponseDto;
import com.example.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> save(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto save = userService.save(requestDto.getEmail(), requestDto.getPassword(), requestDto.getName());
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @ModelAttribute DeleteUserRequestDto requestDto) {
        userService.delete(requestDto.getEmail(), requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
