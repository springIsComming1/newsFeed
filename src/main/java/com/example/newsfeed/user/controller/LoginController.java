package com.example.newsfeed.user.controller;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.user.dto.login.LoginRequestDto;
import com.example.newsfeed.user.dto.login.LoginResponseDto;
import com.example.newsfeed.user.entity.User;
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

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(
            @Valid @ModelAttribute LoginRequestDto requestDto,
            HttpServletRequest request
    ){
        LoginResponseDto responseDto = userService.getUser(requestDto.getEmail());  //responseDto 타입으로 응답하기 위해, 가져온 user 정보를 responsDto에 대입함
        User user = userService.findUser(requestDto.getEmail(), requestDto.getPassword()); //이미 존재하는 유저인지 검증하는 메서드임
        HttpSession session = request.getSession(); //세션 생성
        session.setAttribute(Const.LOGIN_USER, user);   //속성 이름 : LOGIN_USER, 속성 값 : user 객체

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
