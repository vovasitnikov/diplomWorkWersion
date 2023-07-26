package com.github.explore_with_me.main.service;

import com.github.explore_with_me.main.dto.NewUserRequest;
import com.github.explore_with_me.main.dto.UserDto;
import com.github.explore_with_me.main.exception.NotFoundException;
import com.github.explore_with_me.main.model.User;
import com.github.explore_with_me.main.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto create(final NewUserRequest newUserRequest) {
        validate(newUserRequest);
        User user = User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
        return new UserDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsers(final Long[] ids, Integer from, Integer size) {
        List<User> result = userRepository.findAllById(Arrays.asList(ids));
        List<UserDto> users = new ArrayList<>();
        if (result.size() > 0) {
            result.forEach(u -> users.add(new UserDto(u)));
        }
        return users;
    }

    @Transactional
    public void delete(final Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        userRepository.delete(user);
    }

    private void validate(NewUserRequest userDto) {
        if (userDto.getEmail() == null)
            throw new ValidationException("Email cannot be empty.");
        if (userDto.getEmail().isBlank() || !userDto.getEmail().contains("@"))
            throw new ValidationException("Incorrect email: " + userDto.getEmail() + ".");
        if (userDto.getName() == null)
            throw new ValidationException("User name cannot be empty.");
        if (userDto.getName().isBlank())
            throw new ValidationException("User name cannot be empty.");
    }
}
