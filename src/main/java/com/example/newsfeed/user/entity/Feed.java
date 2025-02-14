package com.example.newsfeed.user.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import jakarta.persistence.Column;

public class Feed extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contents;
}
