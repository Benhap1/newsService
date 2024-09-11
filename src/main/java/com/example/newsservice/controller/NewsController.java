package com.example.newsservice.controller;

import com.example.newsservice.dto.CreateNewsDto;
import com.example.newsservice.dto.NewsDto;
import com.example.newsservice.dto.UpdateNewsDto;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "basicScheme")
@RestController
@AllArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<Page<NewsDto>> getAllNews(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") String[] sort) {
        Page<NewsDto> newsPage = newsService.getAllNews(page, size, sort);
        return ResponseEntity.ok(newsPage);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public NewsDto getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id);
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MODERATOR')")
    public NewsDto createNews(@RequestBody CreateNewsDto createNewsDto, Authentication authentication) {
        return newsService.createNews(createNewsDto, authentication.getName());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public NewsDto updateNews(
            @PathVariable Long id,
            @RequestBody UpdateNewsDto updateNewsDto,
            Authentication authentication) {
        return newsService.updateNews(id, updateNewsDto, authentication.getName());
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id, Authentication authentication) {
        newsService.deleteNews(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<List<NewsDto>> getNewsByCategory(@PathVariable Long categoryId) {
        List<NewsDto> newsList = newsService.getNewsByCategory(categoryId);
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<List<NewsDto>> getNewsByUser(@PathVariable Long userId) {
        List<NewsDto> newsList = newsService.getNewsByUser(userId);
        return ResponseEntity.ok(newsList);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')")
    public ResponseEntity<List<NewsDto>> getNewsByCategoryAndUser(
            @RequestParam Long categoryId,
            @RequestParam Long userId) {
        List<NewsDto> newsList = newsService.getNewsByCategoryAndUser(categoryId, userId);
        return ResponseEntity.ok(newsList);
    }
}



