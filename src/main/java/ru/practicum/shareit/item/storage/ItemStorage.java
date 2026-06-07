package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemStorage {
    public List<Item> userItems(Long userId);

    public Item create(Long userId, Item item);

    public Item update(Long userId, Long itemId, ItemDto itemDto);

    public Item removeItem(Long itemId);

    public Item findById(Long itemId);

    public Optional<Item> find(Long id);

    public List<Item> search(String text);
}
