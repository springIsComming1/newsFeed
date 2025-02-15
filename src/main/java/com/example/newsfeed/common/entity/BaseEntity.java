package com.example.newsfeed.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    @CurrentTimestamp
    private LocalDateTime createdAt;

    @LastModifiedDate
    @CurrentTimestamp
    private LocalDateTime modifiedAt;
}
