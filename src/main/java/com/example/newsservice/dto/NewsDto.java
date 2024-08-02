package com.example.newsservice.dto;


import java.time.LocalDateTime;
import java.util.List;


public class NewsDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto user;
    private CategoryDto category;
    private Integer commentCount; // Количество комментариев



    private List<CommentDto> comments;// Список комментариев

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public UserDto getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public Integer getCommentCount() {
        return commentCount;
    }
}
