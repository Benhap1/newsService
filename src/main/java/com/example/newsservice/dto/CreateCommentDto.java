package com.example.newsservice.dto;
public class CreateCommentDto {
    private String content;
    private Long userId;

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    private Long newsId;

    public String getContent() {
        return content;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
