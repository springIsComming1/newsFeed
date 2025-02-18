package com.example.newsfeed.user.controller;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.user.dto.user.UpdatePasswordRequestDto;
import com.example.newsfeed.user.dto.user.UpdateUserRequestDto;
import com.example.newsfeed.user.dto.user.DeleteUserRequestDto;
import com.example.newsfeed.user.dto.user.UserRequestDto;
import com.example.newsfeed.user.dto.user.UserResponseDto;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> save(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto save = userService.save(requestDto.getEmail(), requestDto.getPassword(), requestDto.getName());
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        List<UserResponseDto> userResponseDtoList = userService.findAll(user);

        return new ResponseEntity<>(userResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        UserResponseDto userResponseDto = userService.findById(user, id);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UpdateUserRequestDto requestDto, HttpSession session) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        UserResponseDto userResponseDto = userService.updateUser(user, requestDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/profile/password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UpdatePasswordRequestDto requestDto, HttpSession session) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @ModelAttribute DeleteUserRequestDto requestDto, HttpSession session) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        userService.delete(user, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}