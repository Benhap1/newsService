package com.example.newsservice.dto;


import java.time.LocalDateTime;

public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserDto user;

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserDto getUser() {
        return user;
    }
}