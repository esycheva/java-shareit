package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

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
    public UserDto findById(@PathVariable long userId) {
        return service.findById(userId);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = service.create(userDto);
        log.info("Создан пользователь с именем {}.", createdUser.getName());
        return createdUser;
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        UserDto oldUser = service.update(userId, userDto);
        log.info("Обновлён пользователь с идентификатором {}.", userId);
        return oldUser;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<UserDto> removerUser(@PathVariable Long userId) {
        UserDto userDto = service.removeUser(userId);

        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
