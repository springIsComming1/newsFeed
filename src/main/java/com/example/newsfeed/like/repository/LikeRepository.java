package com.example.newsfeed.like.repository;

import com.example.newsfeed.like.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByPostIdAndStatus(Long id, String postLike);

    Optional<Likes> findByCommentId(Long id);
}
