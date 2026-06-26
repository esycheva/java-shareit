package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

@Service
public interface ItemService {
    ItemDto findById(String itemId);

    Collection<ItemDto> userItems(String userId);

    ItemDto removeItem(Long id);

    ItemDto create(String userId, ItemDto itemDto);

    ItemDto update(String userId, String itemId, ItemDto itemDto);

    Optional<Item> find(Long id);

    Collection<ItemDto> search(String text);

    CommentDto createComment(String userId, String itemId, CommentDto commentDto);

}
