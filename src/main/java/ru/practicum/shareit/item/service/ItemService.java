package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

@Service
public interface ItemService {
    public ItemDto findById(String itemId);

    public Collection<ItemDto> userItems(String userId);

    public ItemDto removeItem(Long id);

    public ItemDto create(String userId, ItemDto itemDto);

    public ItemDto update(String userId, String itemId, ItemDto itemDto);

    public Optional<Item> find(Long id);

    public Collection<ItemDto> search(String text);

    public CommentDto createComment(String userId, String itemId, CommentDto commentDto);

}
