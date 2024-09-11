package com.example.newsservice.dto;
public class CreateCommentDto {
    private String content;

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }
    private Long newsId;
    public String getContent() {
        return content;
    }
    public Long getNewsId() {
        return newsId;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
