package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemStorage storage;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private UserStorage userStorage;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private BookingStorage bookingStorage;

    @InjectMocks
    private ItemServiceImpl service;

    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1L);
        item.setName("Drill");
        item.setDescription("Good drill");
        item.setAvailable(true);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Drill");
        itemDto.setDescription("Good drill");
        itemDto.setAvailable(true);
    }


    @Test
    void findById_shouldReturnItemDtoWithComments() {
        when(storage.findById(1L)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);
        when(storage.findComments(1L)).thenReturn(Collections.emptyList());

        ItemDto result = service.findById("1");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Drill", result.getName());
        assertTrue(result.getComments().isEmpty());

        verify(storage).findById(1L);
        verify(itemMapper).toItemDto(item);
        verify(storage).findComments(1L);
    }

    @Test
    void create_shouldReturnCreatedItemDto() {
        when(itemMapper.toItem(itemDto)).thenReturn(item);
        when(userStorage.findById(1L)).thenReturn(new User());
        when(storage.create(1L, 10L, item)).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        itemDto.setRequestId(10L);

        ItemDto result = service.create("1", itemDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(userStorage).findById(1L);
        verify(storage).create(1L, 10L, item);
    }

    @Test
    void update_shouldReturnUpdatedItemDto() {
        when(userStorage.findById(1L)).thenReturn(new User());
        when(storage.update(eq(1L), eq(2L), any(ItemDto.class))).thenReturn(item);
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemDto result = service.update("1", "2", itemDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(userStorage).findById(1L);
        verify(storage).update(1L, 2L, itemDto);
    }

    @Test
    void userItems_shouldReturnUserItemsWithComments() {
        when(storage.userItems(1L)).thenReturn(List.of(item));
        when(storage.findCommentsByItemIds(List.of(1L))).thenReturn(Collections.emptyList());
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);
        when(bookingStorage.lastBooking(1L)).thenReturn(Optional.empty());
        when(bookingStorage.nextBooking(1L)).thenReturn(Optional.empty());

        var result = service.userItems("1");

        assertEquals(1, result.size());
        ItemDto dto = result.iterator().next();
        assertEquals(1L, dto.getId());
        assertNotNull(dto.getComments());

        verify(storage).userItems(1L);
        verify(bookingStorage).lastBooking(1L);
        verify(bookingStorage).nextBooking(1L);
    }

    @Test
    void createComment_shouldReturnCreatedCommentDto() {
        Comment comment = new Comment();
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Nice item");

        Comment createdComment = new Comment();
        createdComment.setId(1L);
        createdComment.setText("Nice item");

        when(commentMapper.toComment(commentDto)).thenReturn(comment);
        when(userStorage.findById(1L)).thenReturn(new User());
        when(storage.createComment(1L, 2L, comment)).thenReturn(createdComment);
        when(commentMapper.toCommentDto(createdComment)).thenReturn(commentDto);

        CommentDto result = service.createComment("1", "2", commentDto);

        assertNotNull(result);
        assertEquals("Nice item", result.getText());

        verify(userStorage).findById(1L);
        verify(storage).createComment(1L, 2L, comment);
    }

    @Test
    void search_shouldReturnItems() {
        when(storage.search("drill")).thenReturn(List.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        var result = service.search("drill");

        assertEquals(1, result.size());
        verify(storage).search("drill");
    }

}


