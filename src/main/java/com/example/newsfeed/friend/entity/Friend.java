package com.example.newsfeed.friend.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "friend")
public class Friend extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id1")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "user_id2")
    private User requester;

    public Friend(User receiver, User requester) {
        this.receiver = receiver;
        this.requester = requester;
    }
}
