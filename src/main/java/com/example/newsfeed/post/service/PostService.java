package com.example.newsfeed.post.service;

import com.example.newsfeed.post.dto.request.PostSaveRequestDto;
import com.example.newsfeed.post.dto.request.PostUpdateRequestDto;
import com.example.newsfeed.post.dto.response.PostResponseDto;
import com.example.newsfeed.post.dto.response.PostUpdateResponseDto;
import com.example.newsfeed.post.entity.Post;
import com.example.newsfeed.post.repository.PostRepository;
import com.example.newsfeed.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public PostResponseDto save(Long userId, PostSaveRequestDto dto) {
        User user = User.fromUserId(userId);
        Post post = new Post(dto.getTitle(),dto.getContent(),user);//userId,제목,내용을 저장받음
        postRepository.save(post);
        return new PostResponseDto(post.getId(),
                user.getId(),post.getTitle(),post.getContent(),post.getCreatedAt(),post.getModifiedAt());
    }

    @Transactional
    public PostUpdateResponseDto update(Long postId, Long userId, PostUpdateRequestDto dto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다."));//글의 존재여부 체크
        if (!userId.equals(post.getUser().getId())) {//로그인 되어있는 아이디가 현재 선택되어있는 게시글과 userId가 다를경우
            throw new IllegalArgumentException("자신이 작성한 글만 수정할 수 있습니다.");//오류발생
        }
        if(dto.getContent() == null && dto.getTitle()==null) {
            throw new IllegalArgumentException("제목 혹은 내용이 입력되지 않았습니다.");
        }
        if (dto.getTitle() == null) {
            post.update(post.getTitle(),dto.getContent());
        }
        else if (dto.getContent() == null) {
            post.update(dto.getTitle(),post.getContent());
        }
        else {
            post.update(dto.getTitle(), dto.getContent());
        }

        return new PostUpdateResponseDto(post.getId(),post.getUser().getId(),post.getTitle(),post.getContent(),post.getCreatedAt(), post.getModifiedAt());
    }

    public void deleteById(Long scheduleId, Long userId) {
        Post post = postRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다"));
        if (!userId.equals(post.getUser().getId())) {
            throw new IllegalArgumentException("자신이 작성한 글만 삭제할 수 있습니다.");
        }
        postRepository.delete(post);
    }

    public PostResponseDto findOne(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않습니다."));
        return new PostResponseDto(
                post.getId(),post.getUser().getId(),post.getTitle(),post.getContent(),post.getCreatedAt(),post.getModifiedAt());
    }

    public List<PostResponseDto>findAllPage(Integer pageNumber,Integer pageSize) {

        int adjustedPage = (pageNumber > 0) ? pageNumber -1 : 0;
        Pageable pageable = PageRequest.of(adjustedPage,pageSize, Sort.by(Sort.Direction.DESC,"modifiedAt"));
        List<Post> findPostList = postRepository.findAll().stream().toList();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(),findPostList.size());

        if(start >= findPostList.size()) {
            return List.of();
        }

        List<Post> pagedList = findPostList.subList(start,end);

        return pagedList.stream()
                .map(post -> {PostResponseDto postResponseDto = new PostResponseDto(
                        post.getTitle(),
                        post.getContent()
                    );
                    return postResponseDto;
                }).collect(Collectors.toList());
    }
}
