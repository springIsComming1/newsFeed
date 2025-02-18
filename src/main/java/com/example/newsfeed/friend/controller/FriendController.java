package com.example.newsfeed.friend.controller;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.friend.dto.*;
import com.example.newsfeed.friend.service.FriendService;
import com.example.newsfeed.user.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 친구추가 ( 친구신청 )
    @PostMapping("/request/{receiverId}")
    public ResponseEntity<SaveFriendsRequestResponseDto> save(
            @PathVariable Long receiverId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        SaveFriendsRequestResponseDto responseDto = friendService.save(receiverId, user);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 친구 수락
    @PostMapping("/accept/{friendsRequestId}")
    public ResponseEntity<ApproveFriendResponseDto> approve(
            @PathVariable Long friendsRequestId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        ApproveFriendResponseDto responseDto = friendService.approve(friendsRequestId, user);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 친구 거절
    @DeleteMapping("/reject/{friendsRequestId}")
    public ResponseEntity<Void> reject(
            @PathVariable Long friendsRequestId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        friendService.reject(friendsRequestId, user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구 전체 조회
    @GetMapping
    public ResponseEntity<List<ReadAllFriendResponseDto>> findAll(
            HttpSession session
    ) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        List<ReadAllFriendResponseDto> responseDto = friendService.findAll(user);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 친구 요청을 받은 목록 조회 ( 내가 받은 요청 )
    @GetMapping("/requests/received")
    public ResponseEntity<List<ReadFriendRequestReceivedResponseDto>> getReceivedFriendRequests(HttpSession session) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        List<ReadFriendRequestReceivedResponseDto> responseDto = friendService.getReceivedFriendRequests(user);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 친구 요청을 보낸 목록 조회 ( 내가 보낸 요청 )
    @GetMapping("/requests/sent")
    public ResponseEntity<List<ReadFriendRequestSentResponseDto>> getSentFriendRequests(HttpSession session) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        List<ReadFriendRequestSentResponseDto> responseDto = friendService.getSentFriendRequests(user);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 친구 삭제
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long friendId,
            HttpSession session
    ) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        friendService.delete(friendId, user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구의 게시물을 최신순으로 보기
    @GetMapping("/post")
    public ResponseEntity<List<ReadFriendPostResponseDto>> findAllFriendPost(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpSession session
    ) {
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        List<ReadFriendPostResponseDto> readFriendPostResponseDtoList = friendService.findAllFriendPost(pageNumber, pageSize, user);

        return new ResponseEntity<>(readFriendPostResponseDtoList, HttpStatus.OK);
    }
}
