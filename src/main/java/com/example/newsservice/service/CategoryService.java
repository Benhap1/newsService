package com.example.newsservice.service;

import com.example.newsservice.dto.CategoryDto;
import com.example.newsservice.model.Category;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityMapper mapper;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(mapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(mapper::categoryToCategoryDto)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = mapper.categoryDtoToCategory(categoryDto);
        return mapper.categoryToCategoryDto(categoryRepository.save(category));
    }

    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDto.getName());
        return mapper.categoryToCategoryDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
