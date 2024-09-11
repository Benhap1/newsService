package com.example.newsservice.controller;

import com.example.newsservice.dto.CommentDto;
import com.example.newsservice.dto.CreateCommentDto;
import com.example.newsservice.dto.UpdateCommentDto;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.service.CommentService;
import com.example.newsservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/news/{newsId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final NewsRepository newsRepository;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public List<CommentDto> getCommentsByNewsId(@PathVariable Long newsId) {
        return commentService.getCommentsByNewsId(newsId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MODERATOR')")
    public CommentDto createComment(@RequestBody CreateCommentDto createCommentDto, Authentication authentication) {
        String currentUsername = authentication.getName();
        return commentService.createComment(createCommentDto, currentUsername);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public CommentDto updateComment(@PathVariable Long id,
                                    @PathVariable Long newsId,
                                    @RequestBody UpdateCommentDto updateCommentDto,
                                    Authentication authentication) {
        String currentUsername = authentication.getName();
        return commentService.updateComment(id, updateCommentDto, currentUsername, newsId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public void deleteComment(@PathVariable Long id, @PathVariable Long newsId, Authentication authentication) {
        String currentUsername = authentication.getName();
        commentService.deleteComment(id, newsId, currentUsername);
    }
}


