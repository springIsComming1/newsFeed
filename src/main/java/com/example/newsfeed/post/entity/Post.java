package com.example.newsfeed.post.entity;

import com.example.newsfeed.comment.entity.Comment;
import com.example.newsfeed.common.entity.BaseEntity;
import com.example.newsfeed.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Table(name = "post")
@Getter
@Entity
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "longtext")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }


    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
