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

    public Optional<UserDto> findById(long userId) {
        return storage.findById(userId)
                .map(userMapper::toUserDto);
    }

    public Collection<UserDto> findAllUsers() {
        return storage.findAllUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public Optional<User> removeUser(Long id) {
        return storage.removeUser(id);
    }

    public User create(User user) {
        return storage.create(user);
    }

    public User update(Long userId, UserDto userDto) {
        return storage.update(userId, userDto);
    }

    public Optional<User> find(Long id) {
        return storage.find(id);
    }
}
