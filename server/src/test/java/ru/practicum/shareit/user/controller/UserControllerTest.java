package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Получение пользователя по id")
    void findById_ShouldReturnUser() throws Exception {
        UserDto userDto = new UserDto(1L, "Ivan", "ivan@mail.com");

        when(userService.findById(1L)).thenReturn(userDto);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@mail.com"));
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void findAllUsers_ShouldReturnUsers() throws Exception {
        List<UserDto> users = List.of(
                new UserDto(1L, "Ivan", "ivan@mail.com"),
                new UserDto(2L, "Petr", "petr@mail.com")
        );

        when(userService.findAllUsers()).thenReturn(users);

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Ivan"))
                .andExpect(jsonPath("$[0].email").value("ivan@mail.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Petr"))
                .andExpect(jsonPath("$[1].email").value("petr@mail.com"));
    }

    @Test
    @DisplayName("Создание пользователя")
    void create_ShouldReturnCreatedUser() throws Exception {
        UserDto inputDto = new UserDto(null, "Ivan", "ivan@mail.com");
        UserDto outputDto = new UserDto(1L, "Ivan", "ivan@mail.com");

        when(userService.create(any(UserDto.class))).thenReturn(outputDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@mail.com"));
    }

    @Test
    @DisplayName("Обновление пользователя")
    void update_ShouldReturnUpdatedUser() throws Exception {
        UserDto inputDto = new UserDto(null, "Ivan Updated", "ivan.updated@mail.com");
        UserDto outputDto = new UserDto(1L, "Ivan Updated", "ivan.updated@mail.com");

        when(userService.update(eq(1L), any(UserDto.class))).thenReturn(outputDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan Updated"))
                .andExpect(jsonPath("$.email").value("ivan.updated@mail.com"));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void removeUser_ShouldReturnDeletedUser() throws Exception {
        UserDto deletedUser = new UserDto(1L, "Ivan", "ivan@mail.com");

        when(userService.removeUser(anyLong())).thenReturn(deletedUser);

        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.email").value("ivan@mail.com"));
    }
}