package com.example.newsservice.controller;

import com.example.newsservice.dto.CommentDto;
import com.example.newsservice.dto.CreateCommentDto;
import com.example.newsservice.dto.UpdateCommentDto;
import com.example.newsservice.model.User;
import com.example.newsservice.service.CommentService;
import com.example.newsservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/news/{newsId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<CommentDto> getCommentsByNewsId(@PathVariable Long newsId) {
        return commentService.getCommentsByNewsId(newsId);
    }


    @PostMapping
    public CommentDto createComment(@PathVariable Long newsId, @RequestBody CreateCommentDto createCommentDto) {
        createCommentDto.setNewsId(newsId);
        return commentService.createComment(createCommentDto);
    }


//    @PutMapping("/{id}")
//    public CommentDto updateComment(@PathVariable Long id, @RequestBody UpdateCommentDto updateCommentDto) {
//        Long currentUserId = getCurrentUserId();
//        return commentService.updateComment(id, updateCommentDto);
//    }


    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody UpdateCommentDto updateCommentDto) {
        CommentDto updatedComment = commentService.updateComment(id, updateCommentDto);
        return ResponseEntity.ok(updatedComment);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}


