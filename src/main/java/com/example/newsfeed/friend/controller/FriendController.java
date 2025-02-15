package com.example.newsfeed.friend.controller;

import com.example.newsfeed.friend.dto.ApproveFriendResponseDto;
import com.example.newsfeed.friend.dto.ReadAllFriendResponseDto;
import com.example.newsfeed.friend.dto.ReadSelectFriendResponseDto;
import com.example.newsfeed.friend.service.FriendService;
import com.example.newsfeed.friend.dto.SaveFriendsRequestResponseDto;
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

        // 친구 선택 조회
        @GetMapping("/{friendId}")
        public ResponseEntity<ReadSelectFriendResponseDto> findById(@PathVariable Long friendId) {
            ReadSelectFriendResponseDto responseDto = friendService.findById(friendId);

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }
}
