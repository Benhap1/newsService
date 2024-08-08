package com.example.newsservice.controller;

import com.example.newsservice.aspect.CheckNewsAuthor;
import com.example.newsservice.dto.NewsDto;
import com.example.newsservice.dto.CreateNewsDto;
import com.example.newsservice.dto.UpdateNewsDto;
import com.example.newsservice.service.NewsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SecurityRequirement(name = "basicScheme")
@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;


    @GetMapping
    public ResponseEntity<Page<NewsDto>> getAllNews(Pageable pageable) {
        Page<NewsDto> newsPage = newsService.getAllNews(pageable);
        return ResponseEntity.ok(newsPage);
    }


    @GetMapping("/{id}")
    public NewsDto getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id);
    }


    @PostMapping
    public NewsDto createNews(@RequestBody CreateNewsDto createNewsDto) {
        return newsService.createNews(createNewsDto);
    }



    @PutMapping("/{id}")
    @CheckNewsAuthor
    public ResponseEntity<NewsDto> updateNews(@PathVariable Long id, @RequestBody UpdateNewsDto updateNewsDto) {
        NewsDto updatedNews = newsService.updateNews(id, updateNewsDto);
        return ResponseEntity.ok(updatedNews);
    }


    @DeleteMapping("/{id}")
    @CheckNewsAuthor
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }


    // Фильтрация новостей по категории
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<NewsDto>> getNewsByCategory(@PathVariable Long categoryId) {
        List<NewsDto> newsList = newsService.getNewsByCategory(categoryId);
        return ResponseEntity.ok(newsList);
    }

    // Фильтрация новостей по пользователю (автору)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NewsDto>> getNewsByUser(@PathVariable Long userId) {
        List<NewsDto> newsList = newsService.getNewsByUser(userId);
        return ResponseEntity.ok(newsList);
    }

    // Фильтрация новостей по категории и пользователю
    @GetMapping("/filter")
    public ResponseEntity<List<NewsDto>> getNewsByCategoryAndUser(
            @RequestParam Long categoryId,
            @RequestParam Long userId) {
        List<NewsDto> newsList = newsService.getNewsByCategoryAndUser(categoryId, userId);
        return ResponseEntity.ok(newsList);
    }
}


