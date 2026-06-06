package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    public List<User> findAllUsers();

    public User create(User user);

    public User update(Long userId, UserDto userDto);

    public Optional<User> removeUser(Long userId);

    public Optional<User> findById(Long userId);

    public Optional<User> find(Long id);
}
