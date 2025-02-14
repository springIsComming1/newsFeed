package com.example.newsfeed.user.repository;

import com.example.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
}
