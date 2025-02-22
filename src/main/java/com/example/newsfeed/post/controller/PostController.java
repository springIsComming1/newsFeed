package com.example.newsfeed.post.controller;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.post.dto.request.PostUpdateRequestDto;
import com.example.newsfeed.post.dto.response.PostResponseDto;
import com.example.newsfeed.post.dto.response.PostUpdateResponseDto;
import com.example.newsfeed.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> save(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @RequestBody PostSaveRequestDto dto
    ) {
        return ResponseEntity.ok(postService.save(userId, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostUpdateResponseDto> update(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDto dto
            ) {
        return ResponseEntity.ok(postService.update(id, userId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(postService.findOne(id));
    }

    @GetMapping("/page")
    public ResponseEntity<List<PostResponseDto>> findAllPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PostResponseDto> result = postService.findAllPage(page,size);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @SessionAttribute(name = Const.LOGIN_USER) Long userId,
            @PathVariable Long id
    ) {
        postService.deleteById(id, userId);
        return ResponseEntity.ok().build();
    }
}
