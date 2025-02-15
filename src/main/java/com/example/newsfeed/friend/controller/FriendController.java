package com.example.newsfeed.friend.controller;

import com.example.newsfeed.friend.dto.ApproveFriendResponseDto;
import com.example.newsfeed.friend.service.FriendService;
import com.example.newsfeed.friend.dto.SaveFriendsRequestResponseDto;
import com.example.newsfeed.friend.service.FriendsRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//        // 친구 전체 조회
//        @GetMapping
//        public ResponseEntity<ReadFriendResponseDto> findAll(){
//            friendService.findAll();
//        }
    }
}
