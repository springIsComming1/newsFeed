package com.example.newsfeed.friend.controller;

import com.example.newsfeed.friend.dto.*;
import com.example.newsfeed.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;


    @RestController
    @RequestMapping("/friend")
    @RequiredArgsConstructor
    public static class FriendsRequestController {

        private final FriendService friendService;

        // 친구추가 ( 친구신청 )
        @PostMapping
        public ResponseEntity<SaveFriendsRequestResponseDto> save(@RequestParam Long requesterId, Long receiverId){
            SaveFriendsRequestResponseDto responseDto = friendService.save(requesterId, receiverId);

            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        }

        // 친구 수락
        @PostMapping("/{friendsRequestId}")
        public ResponseEntity<ApproveFriendResponseDto> approve(@PathVariable Long friendsRequestId){
            ApproveFriendResponseDto responseDto = friendService.approve(friendsRequestId);

            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        }

        // 친구 전체 조회
        @GetMapping
        public ResponseEntity<List<ReadAllFriendResponseDto>> findAll() {
            List<ReadAllFriendResponseDto> responseDto = friendService.findAll();

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 친구 선택 조회 => 유저 선택 조회가 따로 있으므로 필요없을 것 같습니다.
//        @GetMapping("/{friendId}")
//        public ResponseEntity<ReadSelectFriendResponseDto> findById(@PathVariable Long friendId) {
//            ReadSelectFriendResponseDto responseDto = friendService.findById(friendId);
//
//            return new ResponseEntity<>(responseDto, HttpStatus.OK);
//        }

        // 친구 추가 ( 신청 ) 리스트 조회
        @GetMapping("/{userId}")
        public ResponseEntity<List<ReadFriendRequestResponseDto>> findFriendRequest(@PathVariable Long userId) {
            List<ReadFriendRequestResponseDto> responseDto = friendService.findFriendRequest(userId);

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        // 친구 삭제
        @DeleteMapping("/{friendId}")
        public ResponseEntity<Void> delete(@PathVariable Long friendId){
            friendService.delete(friendId);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
