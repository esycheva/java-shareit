package ru.practicum.shareit.user.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.RecordNotValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;

    @Autowired
    public InMemoryUserStorage() {
        this.users = new HashMap<>();
    }

    public List<User> findAllUsers() {
        return users.values().stream().toList();
    }

    public User create(User user) {
        if (user.validateErrors().size() > 0) {
            String str = user.validateErrors()
                    .stream()
                    .collect(Collectors.joining(","));

            throw new RecordNotValidException(str);
        }

        Optional<User> theSameEmailUser = findByEmail(user.getEmail());

        if(theSameEmailUser.isPresent()) {
            throw new RuntimeException(String.format("Пользователь с email=%s уже существует", user.getEmail()));
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(Long userId, UserDto userDto) {
        if (userId.equals(null)) {
            throw new RecordNotValidException("Id должен быть указан.");
        }

        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь с id=%s не найден.", userId));
        }

        User oldUser = users.get(userId);

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            oldUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {

            Optional<User> theSameEmailUser = findByEmail(userDto.getEmail());

            if(theSameEmailUser.isPresent()) {
                throw new RuntimeException(String.format("Пользователь с email=%s уже существует", userDto.getEmail()));
            }
            oldUser.setEmail(userDto.getEmail());
        }
        return oldUser;
    }

    public Optional<User> findById(Long userId) {
        return find(userId);
    }

    private Long getNextId() {
        Long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Optional<User> find(Long id) {
        return users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public Optional<User> removeUser(Long userId) {
        Optional<User> user = find(userId);
        users.remove(userId);
        return Optional.of(user.get());
    }
}
