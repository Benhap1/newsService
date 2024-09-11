package com.example.newsservice.repository;

import com.example.newsservice.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    List<News> findByCategoryId(Long categoryId);

    List<News> findByUserId(Long userId);

    List<News> findByCategoryIdAndUserId(Long categoryId, Long userId);

    Page<News> findAll(Pageable pageable);
}
