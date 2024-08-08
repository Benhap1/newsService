package com.example.newsservice.service;

import com.example.newsservice.dto.CommentDto;
import com.example.newsservice.dto.CreateCommentDto;
import com.example.newsservice.dto.UpdateCommentDto;
import com.example.newsservice.model.Comment;
import com.example.newsservice.repository.CommentRepository;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityMapper mapper;

    public boolean isAuthor(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        return comment.getUser().getUsername().equals(username);
    }

    public List<CommentDto> getCommentsByNewsId(Long newsId) {
        return commentRepository.findByNewsId(newsId).stream()
                .map(mapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    public CommentDto createComment(CreateCommentDto createCommentDto) {
        Comment comment = new Comment();
        comment.setContent(createCommentDto.getContent());
        comment.setUser(userRepository.findById(createCommentDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        comment.setNews(newsRepository.findById(createCommentDto.getNewsId())
                .orElseThrow(() -> new RuntimeException("News not found")));
        return mapper.commentToCommentDto(commentRepository.save(comment));
    }

    public CommentDto updateComment(Long id, UpdateCommentDto updateCommentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setContent(updateCommentDto.getContent());
        return mapper.commentToCommentDto(commentRepository.save(comment));
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}