package com.example.newsservice.dto;



public class CreateNewsDto {
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
