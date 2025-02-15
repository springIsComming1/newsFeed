package com.example.newsfeed.friend.entity;

import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "friend")
@NoArgsConstructor
public class Friend extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User requester;

    public Friend(User receiver, User requester) {
        this.receiver = receiver;
        this.requester = requester;
    }
}
