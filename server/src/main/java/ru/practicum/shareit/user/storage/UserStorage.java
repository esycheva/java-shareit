package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAllUsers();

    User create(User user);

    User update(Long userId, UserDto userDto);

    User removeUser(Long userId);

    User findById(Long userId);

    Optional<User> find(Long id);
}
