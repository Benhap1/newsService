package com.example.newsservice.service;

import com.example.newsservice.dto.CategoryDto;
import com.example.newsservice.exception.CategoryCreationException;
import com.example.newsservice.exception.ResourceNotFoundException;
import com.example.newsservice.mapper.EntityMapper;
import com.example.newsservice.model.Category;
import com.example.newsservice.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EntityMapper mapper;

    public List<CategoryDto> getAllCategories() {
        List<CategoryDto> categories = categoryRepository.findAll().stream()
                .map(mapper::categoryToCategoryDto)
                .collect(Collectors.toList());
        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("No categories found.");
        }
        return categories;
    }

    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(mapper::categoryToCategoryDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
    }


    public CategoryDto createCategory(CategoryDto categoryDto) {
        try {
            Category category = mapper.categoryDtoToCategory(categoryDto);
            Category savedCategory = categoryRepository.save(category);
            return mapper.categoryToCategoryDto(savedCategory);
        } catch (Exception e) {
            throw new CategoryCreationException("Failed to save category: " + e.getMessage());
        }
    }


    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));

        category.setName(categoryDto.getName());
        return mapper.categoryToCategoryDto(categoryRepository.save(category));
    }


    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
