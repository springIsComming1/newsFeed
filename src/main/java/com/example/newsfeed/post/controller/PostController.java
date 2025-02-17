package com.example.newsfeed.post.controller;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.post.dto.request.PostUpdateRequestDto;
import com.example.newsfeed.post.dto.response.PostSaveResponseDto;
import com.example.newsfeed.post.dto.response.PostUpdateResponseDto;
import com.example.newsfeed.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostSaveResponseDto> save(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @RequestBody PostSaveRequestDto dto
    ) {
        return ResponseEntity.ok(postService.save(userId, dto));
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostUpdateResponseDto> update(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDto dto
            ) {
        return ResponseEntity.ok(postService.update(id, userId, dto));
    }
}
