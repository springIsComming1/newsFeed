package com.example.newsfeed.like.controller;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.like.dto.LikeRequestDto;
import com.example.newsfeed.like.service.LikeService;
import com.example.newsfeed.user.entity.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/post")
    public ResponseEntity<Void> postLike(HttpSession session, @Valid @RequestBody LikeRequestDto requestDto) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);  //LOGIN_USER 라는 이름의 세션 속성 가져와 USER 객체로 캐스팅
        likeService.postLike(user, requestDto.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/comment")
    private ResponseEntity<Void> commentLike(HttpSession session, @RequestBody LikeRequestDto requestDto) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        likeService.commentLike(user, requestDto.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}