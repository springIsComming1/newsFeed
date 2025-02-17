package com.example.newsfeed.comment.controller;

import com.example.newsfeed.comment.dto.CommentFindAllResponseDto;
import com.example.newsfeed.comment.dto.CommentRequestDto;
import com.example.newsfeed.comment.dto.CommentResponseDto;
import com.example.newsfeed.comment.dto.CommentUpdateRequestDto;
import com.example.newsfeed.comment.service.CommentService;
import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.comment.dto.CommentUpdateResponseDto;
import com.example.newsfeed.user.entity.User;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final HttpSession session;

    @PostMapping
    public ResponseEntity<CommentResponseDto> save(@Valid @RequestBody CommentRequestDto requestDto){
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        CommentResponseDto save = commentService.save(user, requestDto.getPostId(), requestDto.getContents()); //어느 게시글에 댓글 달지 postId랑 댓글 내용 넘김
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentFindAllResponseDto>> findAllByPostId(@PathVariable Long postId){    //어느 게시물의 댓글을 보고 싶은지 id 값으로 요청
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        List<CommentFindAllResponseDto> comments = commentService.findAllByPostId(postId, user.getId());
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommentFindAllResponseDto>> findAll() {  //게시물 상관없이 내가 단 댓글 다 보여줌
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        List<CommentFindAllResponseDto> comments = commentService.findAll(user.getId());
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponseDto> update(@PathVariable Long commentId,@Valid @RequestBody CommentUpdateRequestDto requestDto) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);
        CommentUpdateResponseDto update = commentService.update(commentId, requestDto.getContents(), user.getId());
        return new ResponseEntity<>(update, HttpStatus.OK);
    }
}
