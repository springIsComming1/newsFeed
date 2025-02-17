package com.example.newsfeed.comment.repository;

import com.example.newsfeed.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserIdAndBoardId(Long userId, Long boardId);

    default List<Comment> findAllByUserIdAndBoardIdOrElseThrow(Long userId, Long boardId){
        List<Comment> comments = findAllByUserIdAndBoardId(userId, boardId);

        if (comments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다.");
        }

        return comments.stream().toList();
    }
    List<Comment> findAllByUserId(Long id);

    default List<Comment> findAllByUserIdOrElseThrow(Long id){
        List<Comment> comments = findAllByUserId(id);

        if (comments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다.");
        }

        return comments.stream().toList();
    }
}
