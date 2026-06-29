package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;
    private final CommentMapper commentMapper;
    private final BookingStorage bookingStorage;

    @Autowired
    public ItemServiceImpl(@Qualifier("dbItemStorage") ItemStorage storage, @Qualifier("dbUserStorage") UserStorage userStorage, @Qualifier("dbBookingStorage") BookingStorage bookingStorage, ItemMapper itemMapper, CommentMapper commentMapper) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.bookingStorage = bookingStorage;
        this.itemMapper = itemMapper;
        this.commentMapper = commentMapper;
    }

    public ItemDto findById(String itemId) {
        Long itemIdL;
        try {
            itemIdL = Long.parseLong(itemId);
        } catch (NumberFormatException e) {
            throw new NotFoundException("Некорректный id: " + itemId);
        }
        Item item = storage.findById(itemIdL);

        ItemDto itemDto = itemMapper.toItemDto(item);

        List<Comment> comments = storage.findComments(itemIdL);

        itemDto.setComments(comments.stream()
                .map(commentMapper::toCommentDto)
                .toList());

        return itemDto;
    }

    public Collection<ItemDto> userItems(String userId) {
        LocalDateTime now = LocalDateTime.now();

        Long val = Long.parseLong(userId);

        List<Item> items = storage.userItems(val);

        List<Long> itemIds = items.stream().map(Item::getId).toList();

        List<Comment> comments = storage.findCommentsByItemIds(itemIds);

        Map<Long, List<CommentDto>> commentsMap = comments
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.groupingBy(CommentDto::getItemId));

        return items.stream()
                .map(item -> {
                    ItemDto dto = itemMapper.toItemDto(item);

                    bookingStorage.lastBooking(item.getId())
                                    .ifPresent(booking -> dto.setLastBooking(BookingShortDto.builder()
                                            .id(booking.getId())
                                            .bookerId(booking.getBooker().getId())
                                            .build()));

                    bookingStorage.nextBooking(item.getId())
                            .ifPresent(booking -> dto.setNextBooking(BookingShortDto.builder()
                                    .id(booking.getId())
                                    .bookerId(booking.getBooker().getId())
                                    .build()));

                    dto.setComments(commentsMap.getOrDefault(item.getId(), Collections.emptyList()));
                    return dto;
                })
                .toList();
    }

    public ItemDto removeItem(Long id) {
        Item item = storage.removeItem(id);
        return itemMapper.toItemDto(item);
    }

    public ItemDto create(String userId, ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);

        System.out.println(item);

        Long userIdL = Long.parseLong(userId);
        Long requestId = itemDto.getRequestId();

        userStorage.findById(userIdL);

        Item createdItem = storage.create(userIdL, requestId, item);

        return itemMapper.toItemDto(createdItem);
    }

    public CommentDto createComment(String userId, String itemId, CommentDto commentDto) {
        Comment comment = commentMapper.toComment(commentDto);

        Long userIdL = Long.parseLong(userId);
        Long itemIdL = Long.parseLong(itemId);

        userStorage.findById(userIdL);

        Comment createdComment = storage.createComment(userIdL, itemIdL, comment);

        return commentMapper.toCommentDto(createdComment);
    }

    public ItemDto update(String userId, String itemId, ItemDto itemDto) {
        Long usrId = Long.parseLong(userId);
        Long itmId = Long.parseLong(itemId);
        userStorage.findById(usrId);
        Item updatedItem = storage.update(usrId, itmId, itemDto);
        return itemMapper.toItemDto(updatedItem);
    }

    public Optional<Item> find(Long id) {
        return storage.find(id);
    }

    public Collection<ItemDto> search(String text) {
        return storage.search(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
