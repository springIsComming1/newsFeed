package com.example.newsfeed.friend.repository;

import com.example.newsfeed.friend.entity.FriendsRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Repository
public interface FriendsRequestRepository extends JpaRepository<FriendsRequest, Long> {
    Optional<FriendsRequest> findFriendsRequestById(Long friendsRequestId);

    default FriendsRequest findFriendsRequestByIdOrElseThrow(Long friendsRequestId){
        return findFriendsRequestById(friendsRequestId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exists id: " + friendsRequestId));
    }
}
