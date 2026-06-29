package ru.practicum.shareit.user.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Создание пользователя в БД")
    void create_ShouldSaveUserToDb() {
        UserDto userDto = new UserDto(null, "Ivan", "ivan@mail.com");

        UserDto created = userService.create(userDto);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Ivan", created.getName());
        assertEquals("ivan@mail.com", created.getEmail());
    }

    @Test
    @DisplayName("Поиск пользователя по id")
    void findById_ShouldReturnUser() {
        UserDto userDto = new UserDto(null, "Ivan", "ivan@mail.com");
        UserDto created = userService.create(userDto);

        UserDto found = userService.findById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals(created.getName(), found.getName());
        assertEquals(created.getEmail(), found.getEmail());
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void findAllUsers_ShouldReturnAllUsers() {
        userService.create(new UserDto(null, "Ivan", "ivan@mail.com"));
        userService.create(new UserDto(null, "Petr", "petr@mail.com"));

        Collection<UserDto> users = userService.findAllUsers();

        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    @DisplayName("Обновление пользователя")
    void update_ShouldUpdateUser() {
        UserDto created = userService.create(new UserDto(null, "Ivan", "ivan@mail.com"));
        UserDto updateDto = new UserDto(null, "Ivan Updated", "updated@mail.com");

        UserDto updated = userService.update(created.getId(), updateDto);

        assertNotNull(updated);
        assertEquals(created.getId(), updated.getId());
        assertEquals("Ivan Updated", updated.getName());
        assertEquals("updated@mail.com", updated.getEmail());
    }

    @Test
    @DisplayName("Удаление пользователя")
    void removeUser_ShouldDeleteUser() {
        UserDto created = userService.create(new UserDto(null, "Ivan", "ivan@mail.com"));

        UserDto removed = userService.removeUser(created.getId());

        assertNotNull(removed);
        assertEquals(created.getId(), removed.getId());
    }
}
