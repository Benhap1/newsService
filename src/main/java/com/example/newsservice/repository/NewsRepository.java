// NewsRepository.java
package com.example.newsservice.repository;

import com.example.newsservice.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    // Найти новости по ID категории
    List<News> findByCategoryId(Long categoryId);

    // Найти новости по ID пользователя
    List<News> findByUserId(Long userId);

    // Найти новости по ID категории и ID пользователя
    List<News> findByCategoryIdAndUserId(Long categoryId, Long userId);

    // Пагинация новостей
    Page<News> findAll(Pageable pageable);
}
