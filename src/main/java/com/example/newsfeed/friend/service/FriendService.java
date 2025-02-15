package com.example.newsfeed.friend.service;

import com.example.newsfeed.friend.dto.ApproveFriendResponseDto;
import com.example.newsfeed.friend.dto.SaveFriendsRequestResponseDto;
import com.example.newsfeed.friend.entity.Friend;
import com.example.newsfeed.friend.entity.FriendsRequest;
import com.example.newsfeed.friend.repository.FriendRepository;
import com.example.newsfeed.friend.repository.FriendsRequestRepository;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendsRequestRepository friendsRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // 친구추가 ( 친구신청 )
    public SaveFriendsRequestResponseDto save(Long requesterId, Long receiverId) {

        User findRequester = userRepository.findUserByIdOrElseThrow(requesterId);
        User findReceiver = userRepository.findUserByIdOrElseThrow(receiverId);
        String status = "PENDING";

        FriendsRequest friendsRequest = new FriendsRequest(findRequester, findReceiver, status);

        FriendsRequest savedFriendsRequest = friendsRequestRepository.save(friendsRequest);

        return new SaveFriendsRequestResponseDto(savedFriendsRequest.getId(), savedFriendsRequest.getRequester(), savedFriendsRequest.getReceiver(), savedFriendsRequest.getStatus());
    }

    @Transactional
    public ApproveFriendResponseDto approve(Long friendsRequestId) {
        FriendsRequest findFriendsRequest = friendsRequestRepository.findFriendsRequestByIdOrElseThrow(friendsRequestId);

        User findReceiver = userRepository.findUserByIdOrElseThrow(findFriendsRequest.getReceiver().getId());
        User findRequester = userRepository.findUserByIdOrElseThrow(findFriendsRequest.getRequester().getId());

        findFriendsRequest.setStatus("ACCEPTED");

        Friend friend = new Friend(findReceiver, findRequester);
        Friend savedFriend = friendRepository.save(friend);

        return new ApproveFriendResponseDto(savedFriend.getReceiver(), savedFriend.getRequester(), findFriendsRequest.getStatus());
    }

//    public ReadFriendResponseDto findAll() {
//        fr
//    }
}
