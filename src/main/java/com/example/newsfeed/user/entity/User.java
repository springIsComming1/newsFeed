package com.example.newsfeed.user.entity;

import com.example.newsfeed.comment.entity.Comment;
import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.friend.entity.Friend;
import com.example.newsfeed.friend.entity.FriendsRequest;
import com.example.newsfeed.like.entity.Likes;
import com.example.newsfeed.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Likes> likes;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    private List<FriendsRequest> sentFriendsRequests;
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<FriendsRequest> receiveFriendsRequests;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE)
    private List<Friend> receiveFriends;
    @OneToMany(mappedBy = "requester", cascade = CascadeType.REMOVE)
    private List<Friend> requesterFriends;


    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    private User(Long id) {
        this.id = id;
    }

    public static User fromUserId(Long id) {
        return new User(id);
    }
}

