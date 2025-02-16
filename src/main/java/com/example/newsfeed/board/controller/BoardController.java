package com.example.newsfeed.board.controller;

import com.example.newsfeed.board.dto.BoardLikeRequestDto;
import com.example.newsfeed.board.service.BoardService;
import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.user.entity.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Void> like(HttpSession session, @Valid @RequestBody BoardLikeRequestDto requestDto) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);  //LOGIN_USER 라는 이름을 가진 세션 속성 가져와 USER 객체로 캐스팅. 이유는 게시물 안의 userId를 가져오기 위해서이다.
        boardService.like(user.getId(), requestDto.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
