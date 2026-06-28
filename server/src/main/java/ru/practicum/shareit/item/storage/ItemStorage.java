package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> userItems(Long userId);

    Item create(Long userId, Long requestId, Item item);

    Item update(Long userId, Long itemId, ItemDto itemDto);

    Item removeItem(Long itemId);

    Item findById(Long itemId);

    Optional<Item> find(Long id);

    List<Item> search(String text);

    Comment createComment(Long userId, Long itemId, Comment comment);

    List<Comment> findComments(Long itemId);

    List<Comment> findCommentsByItemIds(List<Long> itemIds);
}
