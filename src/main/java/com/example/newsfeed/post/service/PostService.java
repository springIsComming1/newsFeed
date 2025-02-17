package com.example.newsfeed.post.service;

import com.example.newsfeed.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.post.dto.request.PostUpdateRequestDto;
import com.example.newsfeed.post.dto.response.PostSaveResponseDto;
import com.example.newsfeed.post.dto.response.PostUpdateResponseDto;
import com.example.newsfeed.post.entity.Post;
import com.example.newsfeed.post.repository.PostRepository;
import com.example.newsfeed.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

//    @Transactional
//    public PostSaveResponseDto save(Long userId, PostSaveRequestDto dto) {
//        User user = User.fromUserId(userId);
//        Post post = new Post(user, dto.getTitle(),dto.getContent());//userId,제목,내용을 저장받음
//        postRepository.save(post);
//        return new PostSaveResponseDto(post.getId(),
//                user.getId(),post.getTitle(),post.getContent(),post.getCreatedAt(),post.getModifiedAt());
//    }

    @Transactional
    public PostUpdateResponseDto update(Long postId, Long userId, PostUpdateRequestDto dto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다."));//글의 존재여부 체크
        if (!userId.equals(post.getUser().getId())) {//로그인 되어있는 아이디가 현재 선택되어있는 게시글과 userId가 다를경우
            throw new IllegalArgumentException("자신이 작성한 글만 수정할 수 있습니다.");//오류발생
        }
        post.update(dto.getTitle(), dto.getContent());
        return new PostUpdateResponseDto(post.getId(),post.getUser().getId(),post.getTitle(),post.getContent(),post.getCreatedAt(), post.getModifiedAt());

    }
}
