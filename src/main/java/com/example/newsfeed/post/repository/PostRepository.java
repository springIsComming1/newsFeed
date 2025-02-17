package com.example.newsfeed.post.repository;

import com.example.newsfeed.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
