package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(@Qualifier("inMemoryItemStorage") ItemStorage storage, @Qualifier("inMemoryUserStorage") UserStorage userStorage, ItemMapper itemMapper) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.itemMapper = itemMapper;
    }

    public ItemDto findById(String itemId) {
        Long itemIdL;
        try {
            itemIdL = Long.parseLong(itemId);
        } catch (NumberFormatException e) {
            throw new NotFoundException("Некорректный id: " + itemId);
        }
        Item item = storage.findById(itemIdL);
        return itemMapper.toItemDto(item);
    }

    public Collection<ItemDto> userItems(String userId) {
        Long val = Long.parseLong(userId);
        return storage.userItems(val).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public ItemDto removeItem(Long id) {
        Item item = storage.removeItem(id);
        return itemMapper.toItemDto(item);
    }

    public ItemDto create(String userId, ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);

        Long userIdL = Long.parseLong(userId);

        userStorage.findById(userIdL);

        Item createdItem = storage.create(userIdL, item);

        return itemMapper.toItemDto(createdItem);
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
