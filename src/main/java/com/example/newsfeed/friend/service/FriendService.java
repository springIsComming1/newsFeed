package com.example.newsfeed.friend.service;

import com.example.newsfeed.common.consts.Const;
import com.example.newsfeed.friend.dto.*;
import com.example.newsfeed.friend.entity.Friend;
import com.example.newsfeed.friend.entity.FriendsRequest;
import com.example.newsfeed.friend.repository.FriendRepository;
import com.example.newsfeed.friend.repository.FriendsRequestRepository;
import com.example.newsfeed.post.entity.Post;
import com.example.newsfeed.post.repository.PostRepository;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendService {

    private final FriendsRequestRepository friendsRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 친구추가 ( 친구신청 )
    public SaveFriendsRequestResponseDto save(Long receiverId, User requester) {
        if(receiverId == requester.getId()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot send a friend request to yourself.");

        User findReceiver = userRepository.findUserByIdOrElseThrow(receiverId);

        FriendsRequest friendsRequest = new FriendsRequest(requester, findReceiver, Const.STATUS_PENDING);

        FriendsRequest savedFriendsRequest = friendsRequestRepository.save(friendsRequest);

        return new SaveFriendsRequestResponseDto(savedFriendsRequest.getId(), savedFriendsRequest.getRequester().getName(), savedFriendsRequest.getReceiver().getName(), savedFriendsRequest.getStatus());
    }

    // 친구 수락
    @Transactional
    public ApproveFriendResponseDto approve(Long friendsRequestId, User user) {
        FriendsRequest findFriendsRequest = friendsRequestRepository.findFriendsRequestByIdOrElseThrow(friendsRequestId);

        User findReceiver = userRepository.findUserByIdOrElseThrow(findFriendsRequest.getReceiver().getId());
        User findRequester = userRepository.findUserByIdOrElseThrow(findFriendsRequest.getRequester().getId());

        if(!findReceiver.getEmail().equals(user.getEmail())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "친구를 수락할 권한이 없습니다.");

        findFriendsRequest.setStatus(Const.STATUS_ACCEPTED);

        Friend friend = new Friend(findReceiver, findRequester);
        Friend savedFriend = friendRepository.save(friend);

        return new ApproveFriendResponseDto(savedFriend.getReceiver().getName(), savedFriend.getRequester().getName(), findFriendsRequest.getStatus());
    }

    // 친구 거절
    @Transactional
    public RejectFriendResponseDto reject(Long friendsRequestId, User user) {
        FriendsRequest findFriendsRequest = friendsRequestRepository.findFriendsRequestByIdOrElseThrow(friendsRequestId);

        User findReceiver = userRepository.findUserByIdOrElseThrow(findFriendsRequest.getReceiver().getId());
        User findRequester = userRepository.findUserByIdOrElseThrow(findFriendsRequest.getRequester().getId());

        if(!findReceiver.getEmail().equals(user.getEmail())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "친구를 거절할 권한이 없습니다.");

        findFriendsRequest.setStatus(Const.STATUS_REJECTED);

        return new RejectFriendResponseDto(findReceiver.getName(), findRequester.getName(), findFriendsRequest.getStatus());
    }

    // 친구 전체 조회
    public List<ReadAllFriendResponseDto> findAll(User user) {
        String userEmail = user.getEmail();

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

    // 친구 삭제
    @Transactional
    public void delete(Long friendId, User user) {
        String userEmail = user.getEmail();

        Friend findFriend = friendRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getEmail().equals(userEmail) && friend.getRequester().getId() == friendId
        ).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists friend"));

        FriendsRequest findFriendsRequest = friendsRequestRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getEmail().equals(userEmail) && friend.getRequester().getId() == friendId
        ).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists friendsRequest"));

        friendRepository.delete(findFriend);
        findFriendsRequest.setStatus(Const.STATUS_REJECTED);
    }

    // 친구 요청을 받은 목록 조회 ( 내가 받은 요청 )
    public List<ReadFriendRequestReceivedResponseDto> getReceivedFriendRequests(User user) {
        Long userId = user.getId();

        List<FriendsRequest> findFriendsRequestList = friendsRequestRepository.findAll().stream().filter(friendsRequest ->
                friendsRequest.getReceiver().getId() == userId && friendsRequest.getStatus().equals(Const.STATUS_PENDING)
        ).collect(Collectors.toList());

        return findFriendsRequestList.stream()
                .map(findFriendsRequest -> {
                    ReadFriendRequestReceivedResponseDto readFriendRequestResponseDto = new ReadFriendRequestReceivedResponseDto(
                            findFriendsRequest.getRequester().getEmail()
                    );
                    return readFriendRequestResponseDto;
                }).collect(Collectors.toList());
    }

    // 친구 요청을 보낸 목록 조회 ( 내가 보낸 요청 )
    public List<ReadFriendRequestSentResponseDto> getSentFriendRequests(User user) {
        Long userId = user.getId();

        List<FriendsRequest> findFriendsRequestList = friendsRequestRepository.findAll().stream().filter(friendsRequest ->
                friendsRequest.getRequester().getId() == userId && friendsRequest.getStatus().equals(Const.STATUS_PENDING)
        ).collect(Collectors.toList());

        return findFriendsRequestList.stream()
                .map(findFriendsRequest -> {
                    ReadFriendRequestSentResponseDto readFriendRequestSentResponseDto = new ReadFriendRequestSentResponseDto(
                            findFriendsRequest.getReceiver().getEmail()
                    );
                    return readFriendRequestSentResponseDto;
                }).collect(Collectors.toList());
    }

    // 친구의 게시물을 최신순으로 보기
    public List<ReadFriendPostResponseDto> findAllFriendPost(Integer pageNumber, Integer pageSize, User user) {
        String userEmail = user.getEmail();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Long> findFriendIdList = friendRepository.findAll().stream().filter(friend ->
                friend.getReceiver().getEmail().equals(userEmail)
        ).collect(Collectors.toList()).stream().map(friend ->
                friend.getRequester().getId()
        ).collect(Collectors.toList());

        List<Post> findPostList = postRepository.findAll().stream().filter(post ->
                findFriendIdList.contains(post.getUser().getId())
        ).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), findPostList.size());

        if(start >= findPostList.size()){
            return List.of();
        }

        List<Post> pagedList = findPostList.subList(start, end);

        return pagedList.stream()
                .map(post -> {
                    ReadFriendPostResponseDto readFriendPostResponseDto = new ReadFriendPostResponseDto(
                            post.getTitle(),
                            post.getContent(),
                            post.getUser().getName(),
                            post.getCreatedAt()
                    );
                    return readFriendPostResponseDto;
                }).collect(Collectors.toList());
    }

    //친구 여부 확인
    public boolean isFriend(Long myUserId, Long otherUserId) {
        List<List<Long>> friendList = friendRepository.findAll().stream()
                .map(friend -> List.of(friend.getReceiver().getId(), friend.getRequester().getId()))
                .collect(Collectors.toList());

        return friendList.contains(List.of(myUserId, otherUserId)) || friendList.contains(List.of(otherUserId, myUserId));
    }
}
