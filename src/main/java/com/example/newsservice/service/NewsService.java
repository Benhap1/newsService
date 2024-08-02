package com.example.newsservice.service;

import com.example.newsservice.dto.NewsDto;
import com.example.newsservice.dto.CreateNewsDto;
import com.example.newsservice.dto.UpdateNewsDto;
import com.example.newsservice.model.Category;
import com.example.newsservice.model.News;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityMapper mapper;


    //Пагинация метод
    public Page<NewsDto> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable).map(news -> {
            NewsDto newsDto = mapper.newsToNewsDto(news);
            newsDto.setCommentCount(news.getComments() != null ? news.getComments().size() : 0);
            return newsDto;
        });
    }


    public NewsDto getNewsById(Long id) {
        return newsRepository.findById(id)
                .map(news -> {
                    NewsDto newsDto = mapper.newsToNewsDto(news);
                    newsDto.setComments(news.getComments().stream()
                            .map(mapper::commentToCommentDto)
                            .collect(Collectors.toList()));
                    return newsDto;
                })
                .orElseThrow(() -> new RuntimeException("News not found"));
    }


    // Фильтрация новостей по категории
    public List<NewsDto> getNewsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return newsRepository.findByCategoryId(categoryId).stream()
                .map(mapper::newsToNewsDto)
                .collect(Collectors.toList());
    }

    // Фильтрация новостей по пользователю (автору)
    public List<NewsDto> getNewsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return newsRepository.findByUserId(userId).stream()
                .map(mapper::newsToNewsDto)
                .collect(Collectors.toList());
    }

    // Фильтрация новостей по категории и пользователю
    public List<NewsDto> getNewsByCategoryAndUser(Long categoryId, Long userId) {
        return newsRepository.findByCategoryIdAndUserId(categoryId, userId).stream()
                .map(mapper::newsToNewsDto)
                .collect(Collectors.toList());
    }


    public NewsDto createNews(CreateNewsDto createNewsDto) {
        News news = mapper.newsDtoToNews(createNewsDto);
        news.setUser(userRepository.findById(createNewsDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
        news.setCategory(categoryRepository.findById(createNewsDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        return mapper.newsToNewsDto(newsRepository.save(news));
    }


    public NewsDto updateNews(Long id, UpdateNewsDto updateNewsDto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        news.setTitle(updateNewsDto.getTitle());
        news.setContent(updateNewsDto.getContent());

        if (updateNewsDto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(updateNewsDto.getCategoryId());
            news.setCategory(category);
        }

        news.setUpdatedAt(LocalDateTime.now());

        return mapper.newsToNewsDto(newsRepository.save(news));
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}

