package com.example.newsservice.service;

import com.example.newsservice.dto.NewsDto;
import com.example.newsservice.dto.CreateNewsDto;
import com.example.newsservice.dto.UpdateNewsDto;
import com.example.newsservice.exception.ResourceNotFoundException;
import com.example.newsservice.exception.UnauthorizedException;
import com.example.newsservice.model.Category;
import com.example.newsservice.model.News;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.mapper.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final EntityMapper mapper;


    public boolean isAuthor(Long newsId, String username) {
        News news = newsRepository.findById(newsId).orElseThrow(() -> new RuntimeException("News not found"));
        return news.getUser().getUsername().equals(username);
    }


    public Page<NewsDto> getAllNews(int page, int size, String[] sort) {
        Sort sortObj = Sort.by(Sort.Order.by(sort[0].split(",")[0]));
        if (sort[0].contains(",")) {
            String direction = sort[0].split(",")[1];
            if (direction.equalsIgnoreCase("desc")) {
                sortObj = sortObj.descending();
            } else {
                sortObj = sortObj.ascending();
            }
        }

        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<NewsDto> newsPage = newsRepository.findAll(pageable).map(news -> {
            NewsDto newsDto = mapper.newsToNewsDto(news);
            newsDto.setCommentCount(news.getComments() != null ? news.getComments().size() : 0);
            return newsDto;
        });

        if (newsPage.isEmpty()) {
            throw new ResourceNotFoundException("No news found.");
        }

        return newsPage;
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
                .orElseThrow(() -> new ResourceNotFoundException("News with id " + id + " not found"));
    }


    public List<NewsDto> getNewsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + categoryId + " not found"));
        return newsRepository.findByCategoryId(categoryId).stream()
                .map(mapper::newsToNewsDto)
                .collect(Collectors.toList());
    }


    public List<NewsDto> getNewsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        return newsRepository.findByUserId(userId).stream()
                .map(mapper::newsToNewsDto)
                .collect(Collectors.toList());
    }



    public List<NewsDto> getNewsByCategoryAndUser(Long categoryId, Long userId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category with id " + categoryId + " not found.");
        }
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with id " + userId + " not found.");
        }
        List<NewsDto> newsList = newsRepository.findByCategoryIdAndUserId(categoryId, userId).stream()
                .map(mapper::newsToNewsDto)
                .collect(Collectors.toList());
        if (newsList.isEmpty()) {
            throw new ResourceNotFoundException("No news found for the given category and user.");
        }
        return newsList;
    }


    public NewsDto createNews(CreateNewsDto createNewsDto, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        News news = new News();
        news.setTitle(createNewsDto.getTitle());
        news.setContent(createNewsDto.getContent());
        news.setUser(currentUser);
        news.setCategory(categoryRepository.findById(createNewsDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        return mapper.newsToNewsDto(newsRepository.save(news));
    }


    public NewsDto updateNews(Long id, UpdateNewsDto updateNewsDto, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("News with id " + id + " not found"));
        if (currentUser.getRoles().contains("ROLE_USER") && !news.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only update your own news.");
        }
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



    public void deleteNews(Long id, String currentUsername) {
        Optional<User> currentUserOptional = userRepository.findByUsername(currentUsername);
        User currentUser = currentUserOptional.orElseThrow(() ->
                new ResourceNotFoundException("Current user not found"));
        NewsDto news = getNewsById(id);
        if (currentUser.getRoles().contains("ROLE_USER") && !news.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only delete your own news.");
        }
        if (!newsRepository.existsById(id)) {
            throw new ResourceNotFoundException("News with id " + id + " not found");
        }
        newsRepository.deleteById(id);
    }
}


