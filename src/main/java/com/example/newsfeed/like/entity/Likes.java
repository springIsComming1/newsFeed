package com.example.newsfeed.like.entity;

import com.example.newsfeed.comment.entity.Comment;
import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.post.entity.Post;
import com.example.newsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Likes extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Setter
    private String status;

    public Likes(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public Likes(User user, Post post, Comment comment) {
        this.user = user;
        this.post = post;
        this.comment = comment;
    }
}
