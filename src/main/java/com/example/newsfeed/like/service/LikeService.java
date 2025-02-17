package com.example.newsfeed.like.service;

import com.example.newsfeed.board.entity.Board;
import com.example.newsfeed.board.repository.BoardRepository;
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

    public void like(User user, Long boardId) {
        Board board = boardRepository.findById(boardId).    //입력한 boardId 값으로 게시물이 있는지 찾고 없으면 오류 던짐
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시물이 없습니다."));

        if (board.getUser().getId().equals(user.getId())) { //게시물을 작성한 유저의 id와 로그인 되어있는 유저의 id가 같다면 좋아요 못누름.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인의 게시글에는 좋아요를 누를 수 없습니다.");
        }

        Optional<Likes> found = likeRepository.findByUserId(user.getId());  //좋아요가 존재하는지 userId로 찾음.

        if (found.isPresent()) { // 이미 좋아요를 눌렀다면
            likeRepository.delete(found.get()); //좋아요 삭제
            likeRepository.flush(); //즉시 영속성 컨텐츠에 반영된 내용인 delete 를 데이터베이스에 적용
        } else { //좋아요를 누르지 않았다면
            Likes likes = new Likes(user, board);   //좋아요 생성자로 유저정보와 게시물 정보 주입
            likeRepository.save(likes); //좋아요 저장.
        }
    }
}
