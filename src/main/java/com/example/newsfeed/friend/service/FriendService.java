package com.example.newsfeed.friend.service;

import com.example.newsfeed.friend.dto.*;
import com.example.newsfeed.friend.entity.Friend;
import com.example.newsfeed.friend.entity.FriendsRequest;
import com.example.newsfeed.friend.repository.FriendRepository;
import com.example.newsfeed.friend.repository.FriendsRequestRepository;
import com.example.newsfeed.post.entity.Post;
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

        return new SaveFriendsRequestResponseDto(savedFriendsRequest.getId(), savedFriendsRequest.getRequester().getName(), savedFriendsRequest.getReceiver().getName(), savedFriendsRequest.getStatus());
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

        return new ApproveFriendResponseDto(savedFriend.getReceiver().getName(), savedFriend.getRequester().getName(), findFriendsRequest.getStatus());
    }

    // 친구 전체 조회 ( 유저 이메일 받아온다고 가정 )
    public List<ReadAllFriendResponseDto> findAll() {
        String userEmail = "ijieun1@gmail.com";

        List<User> friends = friendRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getEmail().equals(userEmail)
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

    // 친구 선택 조회 ( 유저 이메일 받아온다고 가정 ) => 필요없을 듯
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

    // 친구 추가 ( 신청 ) 리스트 조회 => status = "PENDING" 인 애들만
    public List<ReadFriendRequestResponseDto> findFriendRequest(Long userId) {
        List<FriendsRequest> findFriendsRequestList = friendsRequestRepository.findAll().stream().filter(friendsRequest ->
                friendsRequest.getReceiver().getId() == userId && friendsRequest.getStatus().equals("PENDING")
        ).collect(Collectors.toList());

        return findFriendsRequestList.stream()
                .map(findFriendsRequest -> {
                    ReadFriendRequestResponseDto readFriendRequestResponseDto = new ReadFriendRequestResponseDto(
                            findFriendsRequest.getRequester().getEmail()
                    );
                    return readFriendRequestResponseDto;
                }).collect(Collectors.toList());
    }

    // 친구의 게시물을 최신순으로 보기 ( 유저 이메일 받아온다고 가정 )
    public List<List<ReadFriendPostResponseDto>> findAllFriendPost() {
        String userEmail = "ijieun@gmail.com";

        List<Friend> friendList = friendRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getEmail().equals(userEmail)
        ).collect(Collectors.toList());

        List<List<ReadFriendPostResponseDto>> collect = friendList.stream().map(friend ->
                friend.getRequester().getPosts().stream().map(post -> {
                    ReadFriendPostResponseDto readFriendPostResponseDto = new ReadFriendPostResponseDto(
                            post.getTitle(),
                            post.getContent(),
                            post.getUser().getName()
                    );
                    return readFriendPostResponseDto;
                }).collect(Collectors.toList())
        ).collect(Collectors.toList());

        return collect;
    }
}
