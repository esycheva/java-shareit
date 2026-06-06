package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<UserDto> findAllUsers() {
        return service.findAllUsers();
    }

    @GetMapping("/{userId}")
    public Optional<UserDto> findById(@PathVariable long userId) {
        return service.findById(userId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        User createdUser = service.create(user);
        log.info("Создан пользователь с именем {}.", createdUser.getName());
        return createdUser;
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        User oldUser = service.update(userId, userDto);
        log.info("Обновлён пользователь с идентификатором {}.", userId);
        return oldUser;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<User> removerUser(@PathVariable Long userId) {
        Optional<User> optUser = service.removeUser(userId);

        return optUser.map(user -> ResponseEntity
                .status(HttpStatus.OK)
                .body(user)).orElseGet(() -> ResponseEntity
                .notFound().build());
    }
}
