package com.example.newsfeed.friend.service;

import com.example.newsfeed.friend.dto.ApproveFriendResponseDto;
import com.example.newsfeed.friend.dto.ReadAllFriendResponseDto;
import com.example.newsfeed.friend.dto.ReadSelectFriendResponseDto;
import com.example.newsfeed.friend.dto.SaveFriendsRequestResponseDto;
import com.example.newsfeed.friend.entity.Friend;
import com.example.newsfeed.friend.entity.FriendsRequest;
import com.example.newsfeed.friend.repository.FriendRepository;
import com.example.newsfeed.friend.repository.FriendsRequestRepository;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public List<ReadAllFriendResponseDto> findAll() {
        Long findUserId = userRepository.findUserByEmailOrElseThrow("ijieun@gmail.com").getId();

        List<User> friends = friendRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getId() == findUserId
        ).map(friend ->
            friend.getRequester()
        ).collect(Collectors.toList());

        return friends.stream()
                .map(friend -> {
                    ReadAllFriendResponseDto responseDto = new ReadAllFriendResponseDto(
                            friend.getEmail()
                    );

                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    // 친구 선택 조회 ( 유저 이메일 받아온다고 가정 )
    public ReadSelectFriendResponseDto findById(Long friendId) {
        User findUser = userRepository.findUserByEmailOrElseThrow("ijieun@gmail.com");
        Long findUserId = findUser.getId();

        Friend findFriend = friendRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getId() == findUserId && friend.getRequester().getId() == friendId
        ).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists friend"));

        return new ReadSelectFriendResponseDto(findFriend.getRequester().getEmail(), findFriend.getRequester().getName());
    }

    // 친구 삭제 ( 유저 이메일 받아온다고 가정 )
    @Transactional
    public void delete(Long friendId) {
        User findUser = userRepository.findUserByEmailOrElseThrow("ijieun@gmail.com");
        Long findUserId = findUser.getId();

        Friend findFriend = friendRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getId() == findUserId && friend.getRequester().getId() == friendId
        ).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists friend"));

        FriendsRequest findFriendsRequest = friendsRequestRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getId() == findUserId && friend.getRequester().getId() == friendId
        ).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists friendsRequest"));

        friendRepository.delete(findFriend);
        findFriendsRequest.setStatus("REJECTED");
    }
}
