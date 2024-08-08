package com.example.newsservice.mapper;

import com.example.newsservice.dto.*;
import com.example.newsservice.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    // User
    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);

    // Category
    CategoryDto categoryToCategoryDto(Category category);
    Category categoryDtoToCategory(CategoryDto categoryDTO);

    // News
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "categoryId", target = "category.id")
    News newsDtoToNews(CreateNewsDto createNewsDto);

    @Mapping(target = "commentCount", expression = "java(news.getComments() != null ? news.getComments().size() : 0)")
    @Mapping(target = "comments", ignore = true)
    NewsDto newsToNewsDto(News news);

    // Comment
    CommentDto commentToCommentDto(Comment comment);
    Comment commentDtoToComment(CommentDto commentDTO);
}
