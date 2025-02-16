package com.example.newsfeed.user.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.friend.entity.Friend;
import com.example.newsfeed.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Post> posts = new LinkedHashSet<>();
}
