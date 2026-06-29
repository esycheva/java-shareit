package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserStorage storage;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Ivan");
        user.setEmail("ivan@mail.ru");

        userDto = new UserDto(1L, "Ivan", "ivan@mail.ru");
    }

    @Test
    void findById_ShouldReturnUserDto() {
        when(storage.findById(1L)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());

        verify(storage, times(1)).findById(1L);
        verify(userMapper, times(1)).toUserDto(user);
    }

    @Test
    void findAllUsers_ShouldReturnListOfUserDto() {
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Petr");
        user2.setEmail("petr@mail.ru");

        UserDto userDto2 = new UserDto(2L, "Petr", "petr@mail.ru");

        when(storage.findAllUsers()).thenReturn(List.of(user, user2));
        when(userMapper.toUserDto(user)).thenReturn(userDto);
        when(userMapper.toUserDto(user2)).thenReturn(userDto2);

        var result = userService.findAllUsers();

        assertEquals(2, result.size());
        assertTrue(result.contains(userDto));
        assertTrue(result.contains(userDto2));

        verify(storage, times(1)).findAllUsers();
        verify(userMapper, times(1)).toUserDto(user);
        verify(userMapper, times(1)).toUserDto(user2);
    }

    @Test
    void removeUser_ShouldReturnRemovedUserDto() {
        when(storage.removeUser(1L)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.removeUser(1L);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());

        verify(storage, times(1)).removeUser(1L);
        verify(userMapper, times(1)).toUserDto(user);
    }

    @Test
    void create_ShouldReturnCreatedUserDto() {
        User inputUser = new User();
        inputUser.setName("Ivan");
        inputUser.setEmail("ivan@mail.ru");

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setName("Ivan");
        createdUser.setEmail("ivan@mail.ru");

        when(userMapper.toItem(userDto)).thenReturn(inputUser);
        when(storage.create(inputUser)).thenReturn(createdUser);
        when(userMapper.toUserDto(createdUser)).thenReturn(userDto);

        UserDto result = userService.create(userDto);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());

        verify(userMapper, times(1)).toItem(userDto);
        verify(storage, times(1)).create(inputUser);
        verify(userMapper, times(1)).toUserDto(createdUser);
    }

    @Test
    void update_ShouldReturnUpdatedUserDto() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Ivan Updated");
        updatedUser.setEmail("ivan.updated@mail.ru");

        UserDto updatedDto = new UserDto(1L, "Ivan Updated", "ivan.updated@mail.ru");

        when(storage.update(1L, userDto)).thenReturn(updatedUser);
        when(userMapper.toUserDto(updatedUser)).thenReturn(updatedDto);

        UserDto result = userService.update(1L, userDto);

        assertNotNull(result);
        assertEquals(updatedDto.getId(), result.getId());
        assertEquals(updatedDto.getName(), result.getName());
        assertEquals(updatedDto.getEmail(), result.getEmail());

        verify(storage, times(1)).update(1L, userDto);
        verify(userMapper, times(1)).toUserDto(updatedUser);
    }

    @Test
    void find_ShouldReturnOptionalUser() {
        when(storage.find(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.find(1L);

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        assertEquals(user.getName(), result.get().getName());
        assertEquals(user.getEmail(), result.get().getEmail());

        verify(storage, times(1)).find(1L);
    }
}