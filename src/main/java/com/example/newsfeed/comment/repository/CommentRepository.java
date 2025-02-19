package com.example.newsfeed.comment.repository;

import com.example.newsfeed.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserIdAndPostId(Long userId, Long postId);

    default List<Comment> findAllByUserIdAndPostIdOrElseThrow(Long userId, Long postId){
        List<Comment> comments = findAllByUserIdAndPostId(userId, postId);

        if (comments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다.");
        }

        return comments.stream().toList();
    }

    Page<Comment> findAllByPostId(Long id, Pageable pageable);


    List<Comment> findAllByUserId(Long id);

    default List<Comment> findAllByUserIdOrElseThrow(Long id){
        List<Comment> comments = findAllByUserId(id);

        if (comments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다.");
        }

        return comments.stream().toList();
    }
}
