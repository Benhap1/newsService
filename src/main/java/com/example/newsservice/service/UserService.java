package com.example.newsservice.service;

import com.example.newsservice.dto.CreateUserDto;
import com.example.newsservice.dto.UpdateUserDto;
import com.example.newsservice.dto.UserDto;
import com.example.newsservice.mapper.EntityMapper;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityMapper mapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(mapper::userToUserDto)
                .collect(Collectors.toList());
    }


    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(mapper::userToUserDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDto createUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword())); // Шифрование пароля
        user.setEmail(createUserDto.getEmail());

        User savedUser = userRepository.save(user);
        return mapper.userToUserDto(savedUser);
    }

    public UserDto updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(updateUserDto.getUsername());
        user.setEmail(updateUserDto.getEmail());
        return mapper.userToUserDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
