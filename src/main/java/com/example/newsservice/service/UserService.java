package com.example.newsservice.service;

import com.example.newsservice.dto.AssignRoleDto;
import com.example.newsservice.dto.CreateUserDto;
import com.example.newsservice.dto.UpdateUserDto;
import com.example.newsservice.dto.UserDto;
import com.example.newsservice.exception.ResourceNotFoundException;
import com.example.newsservice.exception.UnauthorizedException;
import com.example.newsservice.mapper.EntityMapper;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper mapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(mapper::userToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(mapper::userToUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

    public UserDto createUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setEmail(createUserDto.getEmail());

        User savedUser = userRepository.save(user);
        return mapper.userToUserDto(savedUser);
    }

    public UserDto updateUser(Long id, UpdateUserDto updateUserDto, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        if (currentUser.getRoles().contains("ROLE_USER") && !currentUser.getId().equals(id)) {
            throw new UnauthorizedException("You can only update your own profile.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        user.setUsername(updateUserDto.getUsername());
        user.setEmail(updateUserDto.getEmail());
        return mapper.userToUserDto(userRepository.save(user));
    }

    public void deleteUser(Long id, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        if (currentUser.getRoles().contains("ROLE_USER") && !currentUser.getId().equals(id)) {
            throw new UnauthorizedException("You can only delete your own profile.");
        }

        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        userRepository.deleteById(id);
    }

        public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public void assignRoleToUser(AssignRoleDto assignRoleDto) {
        User user = userRepository.findById(assignRoleDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + assignRoleDto.getUserId() + " not found"));
        String newRole = assignRoleDto.getRole();
        user.getRoles().clear();
        user.getRoles().add(newRole);
        userRepository.save(user);
    }
}


