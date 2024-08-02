// CommentRepository.java
package com.example.newsservice.repository;

import com.example.newsservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByNewsId(Long newsId);
}
