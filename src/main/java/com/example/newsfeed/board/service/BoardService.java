package com.example.newsfeed.board.service;

import com.example.newsfeed.board.entity.Board;
import com.example.newsfeed.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void like(Long userId, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 피드입니다.")); // 요청한 게시물 id가 존재하는 게시물인지 찾음

        if(board.getUser().getId().equals(userId)){    //피드 작성한 본인인가?
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "본인의 게시물에는 좋아요를 남길 수 없습니다.");
        }

        board.setLikes(board.getLikes() + 1);   //게시글의 좋아요 1증가.
        boardRepository.save(board);
    }
}
