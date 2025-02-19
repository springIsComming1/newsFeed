package com.example.newsfeed.comment.service;

import com.example.newsfeed.comment.dto.*;
import com.example.newsfeed.comment.entity.Comment;
import com.example.newsfeed.comment.repository.CommentRepository;
import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.post.entity.Post;
import com.example.newsfeed.post.repository.PostRepository;
import com.example.newsfeed.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto save(User user, Long postId, String contents) {
        Post post = postRepository.findById(postId) //보낸 postId가 없을 경우
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 없습니다."));

        Comment comment = new Comment(contents, post, user);   //댓글 생성자로 어느 게시글에 누가 달았는지 알려고 post, user 도 파라미터로 넣음
        commentRepository.save(comment);
        return new CommentResponseDto(comment.getId(), comment.getContents());
    }

    public List<CommentFindAllMineResponseDto> findAllMineByPostId(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 없습니다."));    //요청한 게시물이 있는지 찾음

        List<Comment> comments = commentRepository.findAllByUserIdAndPostIdOrElseThrow(userId, post.getId());    //유저 id와 게시물 id로 어떤 게시물에 내가 댓글 작성했는지 List<Comment> 타입으로 찾음.
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed()  //먼저 최신 댓글 기준으로 정렬하면서 그 다음은 수정일 기준으로 정렬.
                        .thenComparing(Comment::getModifiedAt))
                .map(comment -> new CommentFindAllMineResponseDto(  //찾은 List 타입의 댓글들을 responseDto 로 변환해서 응답
                        comment.getId(),
                        comment.getContents(),
                        comment.getPost().getId()
                )).collect(Collectors.toList());
    }

    public PagedCommentResponseDto findAllByPostId(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));  //페이지네이션 하는겸 정렬
        Page<Comment> comments = commentRepository.findAllByPostId(id, pageable);   //찾은걸 Page 형태로 리턴

        if (comments.isEmpty()) {   //없으면 예외처리
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다.");
        }

        List<CommentFindAllByPostResponseDto> commentList = comments.stream()   //이렇게 하면 responseDto 가 두개라 싫어서 다른 방법으로 Comment 에 생성자 만들어서 List<Comment> 로 해봤는데 그때는 Comment 필드의 다른 필요없는 값도 응답에 null 로 튀어나옴 (예를 들면 user 필드 같은거) 그래서 dto 만들어서 필요한 것만 넣음
                .map(comment -> new CommentFindAllByPostResponseDto(
                        comment.getId(),
                        comment.getContents()
                )).toList();

        return new PagedCommentResponseDto(commentList, comments.getNumber() + 1, comments.getTotalPages());
    }

    public List<CommentFindAllMineResponseDto> findAll(Long id) {
        List<Comment> comments = commentRepository.findAllByUserIdOrElseThrow(id); //내가 작성한 댓글있나 userId로 찾아줌

        return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed()  //먼저 최신 댓글 기준으로 정렬하면서 그 다음은 수정일 기준으로 정렬.
                        .thenComparing(Comment::getModifiedAt))
                .map(comment -> new CommentFindAllMineResponseDto(  //찾은 List 타입의 댓글들을 responseDto 로 변환해서 응답
                        comment.getId(),
                        comment.getContents(),
                        comment.getPost().getId()
                )).toList();
    }

    public CommentUpdateResponseDto update(Long commentId, String contents, Long userId) {
        Comment comment = commentRepository.findById(commentId) //댓글 id로 댓글 있나 찾기
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다."));

        if(!userId.equals(comment.getUser().getId())){  //본인이 단 댓글인지 비교하는 부분
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "댓글을 단 본인만 댓글의 수정이 가능합니다.");
        }

        comment.setContents(contents);  //댓글 내용 수정
        commentRepository.save(comment);    //수정된 댓글 데이터베이스에 저장

        return new CommentUpdateResponseDto(comment.getId() ,comment.getPost().getId(), comment.getContents());
    }

    public void delete(Long commentId, Long userId) {
        //댓글 ID로 해당 댓글 찾기 (없으면 예외 발생)
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));

        //댓글 작성자의 ID
        Long commentWriterId = comment.getUser().getId();

        //해당 댓글이 속한 게시글의 작성자의 ID
        Long postWriterId = comment.getPost().getUser().getId();

        //댓글 작성자 또는 게시글 작성자인 경우만 삭제 가능
        if (!userId.equals(commentWriterId) && !userId.equals(postWriterId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "댓글 작성자 또는 게시글 작성자만 삭제할 수 있습니다.");
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }


}