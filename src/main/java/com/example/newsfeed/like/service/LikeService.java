package com.example.newsfeed.like.service;

import com.example.newsfeed.board.entity.Board;
import com.example.newsfeed.board.repository.BoardRepository;
import com.example.newsfeed.comment.entity.Comment;
import com.example.newsfeed.comment.repository.CommentRepository;
import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.like.entity.Likes;
import com.example.newsfeed.like.repository.LikeRepository;
import com.example.newsfeed.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public void boardLike(User user, Long boardId) {
        Board board = boardRepository.findById(boardId).    //입력한 boardId 값으로 게시물이 있는지 찾고 없으면 오류 던짐
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 없습니다."));

        if (board.getUser().getId().equals(user.getId())) { //게시물을 작성한 유저의 id와 로그인 되어있는 유저의 id가 같다면 좋아요 못누름.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인의 게시글에는 좋아요를 누를 수 없습니다.");
        }

        Optional<Likes> found = likeRepository.findByBoardIdAndStatus(board.getId(), Const.BOARD_LIKE);  //좋아요가 존재하는지 boardId와 status 로 찾음.

        if (found.isPresent() && found.get().getStatus().equals(Const.BOARD_LIKE) && found.get().getBoard().getId().equals(board.getId())) { // 이미 좋아요를 눌렀으며, 스테이터스가 '게시물 좋아요' 이고, 좋아요의 게시물 번호와 내가 입력한 게시물 번호가 같으면
            likeRepository.delete(found.get()); //좋아요 삭제
            likeRepository.flush(); //즉시 영속성 컨텐츠에 반영된 내용인 delete 를 데이터베이스에 적용
        } else { //게시글 좋아요를 누르지 않았다면
            Likes likes = new Likes(user, board);   //좋아요 생성자로 유저정보와 게시물 정보 주입
            likes.setStatus(Const.BOARD_LIKE);  //좋아요의 스테이터스를 게시글 좋아요로 저장
            likeRepository.save(likes); //좋아요 저장.
        }
    }

    public void commentLike(User user, Long commentId) {    
        Comment comment = commentRepository.findById(commentId) //입력한 commentId 값으로 댓글이 있는지 찾고 없으면 오류 던짐
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 없습니다"));

        if (comment.getUser().getId().equals(user.getId())) {   //본인의 댓글에는 좋아요를 본인이 누를 수 없음. (댓글을 작성한 userId와 로그인 되어 있는 userId가 같은지 검증)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인의 댓글에는 좋아요를 누를 수 없습니다.");
        }

        Optional<Likes> found = likeRepository.findByCommentId(comment.getId());    //좋아요가 존재하는지 댓글 id로 찾음

        if (found.isPresent() && found.get().getStatus().equals(Const.COMMENT_LIKE) && found.get().getComment().getId().equals(comment.getId())) {  //좋아요를 이미 눌렀으며, 좋아요의 스테이터스가 '댓글 좋아요' 이고,좋아요의 댓글의 id가 내가 입력한 id와 같으면
            likeRepository.delete(found.get()); //좋아요 삭제
            likeRepository.flush();
        } else { //댓글 좋아요를 누르지 않았다면
            Likes likes = new Likes(user, comment.getBoard(), comment); //좋아요 생성자의 파라미터로 누가 누른지 알기위해  user, 어느 게시물에 달렸는지 알기 위해 board, 어느 댓글인지 알기위해 comment 저장
            likes.setStatus(Const.COMMENT_LIKE);    //좋아요의 스테이터스를 '댓글 좋아요'로 저장
            likeRepository.save(likes);
        }
    }
}
