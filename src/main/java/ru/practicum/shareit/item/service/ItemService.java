package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

@Service
public interface ItemService {
    public Optional<ItemDto> findById(long itemId);

    public Collection<ItemDto> userItems(String userId);

    public Optional<Item> removeItem(Long id);

    public Item create(String userId, ItemDto itemDto);

    public Item update(String userId, String itemId, ItemDto itemDto);

    public Optional<Item> find(Long id);

    public Collection<ItemDto> search(String text);
}
