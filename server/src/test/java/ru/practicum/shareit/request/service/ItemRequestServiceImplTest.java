package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private UserStorage userStorage;

    @Mock
    private ItemRequestStorage storage;

    @InjectMocks
    private ItemRequestServiceImpl service;

    private ItemRequestDto requestDto;
    private ItemRequest request;
    private ItemRequest createdRequest;

    @BeforeEach
    void setUp() {
        requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");

        request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need a drill");

        createdRequest = new ItemRequest();
        createdRequest.setId(2L);
        createdRequest.setDescription("Need a drill");
    }

    @Test
    void create_whenUserExists_thenReturnCreatedRequestDto() {
        User user = new User();
        user.setId(1L);

        when(itemRequestMapper.toItemRequest(requestDto)).thenReturn(request);
        when(userStorage.findById(1L)).thenReturn(user);
        when(storage.create(eq(1L), eq(request))).thenReturn(createdRequest);
        when(itemRequestMapper.toItemRequestDto(createdRequest)).thenReturn(requestDto);

        ItemRequestDto result = service.create("1", requestDto);

        assertNotNull(result);
        assertEquals("Need a drill", result.getDescription());

        verify(userStorage).findById(1L);
        verify(storage).create(1L, request);
        verify(itemRequestMapper).toItemRequest(requestDto);
        verify(itemRequestMapper).toItemRequestDto(createdRequest);
    }

    @Test
    void create_whenUserNotFound_thenThrowException() {
        when(itemRequestMapper.toItemRequest(requestDto)).thenReturn(request);
        when(userStorage.findById(1L)).thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> service.create("1", requestDto));

        verify(storage, never()).create(anyLong(), any());
    }

    @Test
    void findById_shouldReturnRequestDto() {
        when(storage.findById(1L)).thenReturn(request);
        when(itemRequestMapper.toItemRequestDto(request)).thenReturn(requestDto);

        ItemRequestDto result = service.findById("1", "1");

        assertNotNull(result);
        assertEquals("Need a drill", result.getDescription());

        verify(storage).findById(1L);
        verify(itemRequestMapper).toItemRequestDto(request);
    }

    @Test
    void getItemRequestsByRequestor_shouldReturnList() {
        ItemRequest secondRequest = new ItemRequest();
        secondRequest.setId(2L);
        secondRequest.setDescription("Need a saw");

        ItemRequestDto secondDto = new ItemRequestDto();
        secondDto.setDescription("Need a saw");

        when(storage.getItemRequestorsByRequestor(1L)).thenReturn(List.of(request, secondRequest));
        when(itemRequestMapper.toItemRequestDto(request)).thenReturn(requestDto);
        when(itemRequestMapper.toItemRequestDto(secondRequest)).thenReturn(secondDto);

        List<ItemRequestDto> result = service.getItemRequestsByRequestor("1");

        assertEquals(2, result.size());
        assertEquals("Need a drill", result.get(0).getDescription());
        assertEquals("Need a saw", result.get(1).getDescription());
    }

    @Test
    void findAllItemRequests_shouldReturnAllRequests() {
        when(storage.findAllItemRequests()).thenReturn(List.of(request));
        when(itemRequestMapper.toItemRequestDto(request)).thenReturn(requestDto);

        var result = service.findAllItemRequests();

        assertEquals(1, result.size());
        assertEquals("Need a drill", result.iterator().next().getDescription());
    }
}
