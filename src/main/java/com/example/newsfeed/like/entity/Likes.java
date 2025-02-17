package com.example.newsfeed.like.entity;

import com.example.newsfeed.board.entity.Board;
import com.example.newsfeed.comment.entity.Comment;
import com.example.newsfeed.common.entity.BaseEntity;
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
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Setter
    private String status;

    public Likes(User user, Board board) {
        this.user = user;
        this.board = board;
    }

    public Likes(User user, Board board, Comment comment) {
        this.user = user;
        this.board = board;
        this.comment = comment;
    }
}
