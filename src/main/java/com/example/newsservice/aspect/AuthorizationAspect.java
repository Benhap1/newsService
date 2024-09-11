package com.example.newsservice.aspect;

import com.example.newsservice.exception.UnauthorizedException;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AuthorizationAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;

    @Before("@annotation(com.example.newsservice.aspect.CheckNewsAuthor) && args(newsId,..)")
    public void checkNewsAuthor(Long newsId) {
        String username = request.getUserPrincipal().getName();
        if (!newsService.isAuthor(newsId, username)) {
            throw new UnauthorizedException("User is not the author of this news");
        }
    }
    @Before("@annotation(com.example.newsservice.aspect.CheckCommentAuthor) && args(commentId,..)")
    public void checkCommentAuthor(Long commentId) {
        String username = request.getUserPrincipal().getName();
        if (!commentService.isAuthor(commentId, username)) {
            throw new UnauthorizedException("User is not the author of this comment");
        }
    }
}