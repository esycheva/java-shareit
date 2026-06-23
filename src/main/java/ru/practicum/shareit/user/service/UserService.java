package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage storage;
    private final UserMapper userMapper;

    @Autowired
    public UserService(@Qualifier("inMemoryUserStorage") UserStorage storage, UserMapper userMapper) {
        this.storage = storage;
        this.userMapper = userMapper;
    }

    public UserDto findById(long userId) {
        User user = storage.findById(userId);
        return userMapper.toUserDto(user);
    }

    public Collection<UserDto> findAllUsers() {
        return storage.findAllUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto removeUser(Long id) {
        User user = storage.removeUser(id);
        return userMapper.toUserDto(user);
    }

    public UserDto create(UserDto userDto) {
        User user = userMapper.toItem(userDto);
        User createdUser = storage.create(user);
        return userMapper.toUserDto(createdUser);
    }

    public UserDto update(Long userId, UserDto userDto) {
        User updatedUser = storage.update(userId, userDto);
        return userMapper.toUserDto(updatedUser);
    }

    public Optional<User> find(Long id) {
        return storage.find(id);
    }
}
