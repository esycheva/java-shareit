package ru.practicum.shareit.user.storage;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.RecordNotValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("dbUserStorage")
public class DbUserStorage implements UserStorage {
    private final UserRepository userRepository;

    public DbUserStorage(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User create(User user) {
        if (user.validateErrors().size() > 0) {
            String str = user.validateErrors()
                    .stream()
                    .collect(Collectors.joining(","));

            throw new RecordNotValidException(str);
        }

        Optional<User> theSameEmailUser = findByEmail(user.getEmail());

        if (theSameEmailUser.isPresent()) {
            throw new RuntimeException(String.format("Пользователь с email=%s уже существует", user.getEmail()));
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(Long userId, UserDto userDto) {
        if (userId.equals(null)) {
            throw new RecordNotValidException("Id должен быть указан.");
        }

        User oldUser = find(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь не найден с id: %s", userId)));

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            oldUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {

            Optional<User> theSameEmailUser = findByEmail(userDto.getEmail());

            if (theSameEmailUser.isPresent()) {
                throw new RuntimeException(String.format("Пользователь с email=%s уже существует", userDto.getEmail()));
            }
            oldUser.setEmail(userDto.getEmail());
        }
        return userRepository.save(oldUser);
    }

    @Override
    @Transactional
    public User removeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(String.format("Пользователь не найден с id: %s", userId)));

        userRepository.delete(user);
        return user;
    }

    @Override
    public Optional<User> find(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
