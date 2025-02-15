package com.example.newsfeed.friend.service;

import com.example.newsfeed.friend.dto.ApproveFriendResponseDto;
import com.example.newsfeed.friend.dto.ReadFriendResponseDto;
import com.example.newsfeed.friend.dto.SaveFriendsRequestResponseDto;
import com.example.newsfeed.friend.entity.Friend;
import com.example.newsfeed.friend.entity.FriendsRequest;
import com.example.newsfeed.friend.repository.FriendRepository;
import com.example.newsfeed.friend.repository.FriendsRequestRepository;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
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

    // 친구 수락
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

    // 친구 전체 조회 ( 유저 이메일 받아온다고 가정 )
    public List<ReadFriendResponseDto> findAll() {
        Long findUserId = userRepository.findUserByEmailOrElseThrow("ijieun@gmail.com").getId();

        List<User> friends = friendRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getId() == findUserId
        ).map(friend ->
            friend.getRequester()
        ).collect(Collectors.toList());

        return friends.stream()
                .map(friend -> {
                    ReadFriendResponseDto responseDto = new ReadFriendResponseDto(
                            friend.getId(),
                            friend.getName(),
                            friend.getEmail()
                    );

                    return responseDto;
                })
                .collect(Collectors.toList());
    }
}
