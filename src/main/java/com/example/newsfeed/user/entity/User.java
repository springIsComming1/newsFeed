package com.example.newsfeed.user.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.friend.entity.Friend;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;

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

}
