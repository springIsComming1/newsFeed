package com.example.newsfeed.post.dto.response;

import com.example.newsfeed.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {
    private  Long id;
    private  Long userId;
    private  String title;
    private  String content;
    private  LocalDateTime createdAt;
    private  LocalDateTime modifiedAt;

    public PostResponseDto(Long id, Long userId, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public PostResponseDto(String title,String content) {
        this.title = title;
        this.content = content;
    }

    public static PostResponseDto toDto(Post post) {
        return new PostResponseDto(post.getTitle(),post.getContent());
    }
}
