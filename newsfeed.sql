CREATE DATABASE newsfeed;
USE newsfeed;

-- 사용자 테이블
CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(50) UNIQUE NOT NULL,
                      email VARCHAR(100) UNIQUE NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 게시글 테이블
CREATE TABLE post (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      user_id BIGINT NOT NULL,
                      content TEXT NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- 댓글 테이블
CREATE TABLE comment (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         post_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         content TEXT NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE,
                         FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- 좋아요 테이블
CREATE TABLE user_like (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           user_id BIGINT NOT NULL,
                           post_id BIGINT NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                           FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
);

-- 친구 관계 테이블 (승인된 친구만 저장)
CREATE TABLE friend (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        friend_id BIGINT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                        FOREIGN KEY (friend_id) REFERENCES user(id) ON DELETE CASCADE
);
