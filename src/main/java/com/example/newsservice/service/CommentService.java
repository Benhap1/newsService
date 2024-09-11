package com.example.newsservice.service;

import com.example.newsservice.dto.CommentDto;
import com.example.newsservice.dto.CreateCommentDto;
import com.example.newsservice.dto.UpdateCommentDto;
import com.example.newsservice.exception.ResourceNotFoundException;
import com.example.newsservice.exception.UnauthorizedException;
import com.example.newsservice.mapper.EntityMapper;
import com.example.newsservice.model.Comment;
import com.example.newsservice.model.News;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.CommentRepository;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final NewsRepository newsRepository;

    private final UserRepository userRepository;

    private final EntityMapper mapper;

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public boolean isAuthor(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        return comment.getUser().getUsername().equals(username);
    }

    public List<CommentDto> getCommentsByNewsId(Long newsId) {
        boolean newsExists = newsRepository.existsById(newsId);
        if (!newsExists) {
            throw new ResourceNotFoundException("News with id " + newsId + " not found");
        }
        return commentRepository.findByNewsId(newsId).stream()
                .map(mapper::commentToCommentDto)
                .collect(Collectors.toList());
    }


    public CommentDto createComment(CreateCommentDto createCommentDto, String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        Comment comment = new Comment();
        comment.setContent(createCommentDto.getContent());
        comment.setUser(currentUser);
        News news = newsRepository.findById(createCommentDto.getNewsId())
                .orElseThrow(() -> new ResourceNotFoundException("News with id " + createCommentDto.getNewsId() + " not found"));
        comment.setNews(news);
        return mapper.commentToCommentDto(commentRepository.save(comment));
    }


    public CommentDto updateComment(Long id, UpdateCommentDto updateCommentDto, String currentUsername, Long newsId) {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!newsRepository.existsById(newsId)) {
            throw new ResourceNotFoundException("News with id " + newsId + " not found");
        }
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found for news with id " + newsId));
        if (!comment.getNews().getId().equals(newsId)) {
            throw new ResourceNotFoundException("Comment with id " + id + " does not belong to news with id " + newsId);
        }
        if (!comment.getUser().getId().equals(currentUser.getId()) &&
                !currentUser.getRoles().contains("ROLE_ADMIN") &&
                !currentUser.getRoles().contains("ROLE_MODERATOR")) {
            throw new UnauthorizedException("You can only update your own comments or must be a moderator/admin.");
        }
        comment.setContent(updateCommentDto.getContent());
        return mapper.commentToCommentDto(commentRepository.save(comment));
    }


    public void deleteComment(Long id, Long newsId, String currentUsername) {
        if (!newsRepository.existsById(newsId)) {
            throw new ResourceNotFoundException("News with id " + newsId + " not found");
        }
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with id " + id + " not found"));
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        if (!comment.getUser().getId().equals(currentUser.getId()) &&
                !currentUser.getRoles().contains("ROLE_ADMIN") &&
                !currentUser.getRoles().contains("ROLE_MODERATOR")) {
            throw new UnauthorizedException("You do not have permission to delete this comment.");
        }
        commentRepository.deleteById(id);
    }
}