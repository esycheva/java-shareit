package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
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

    public Optional<ItemDto> findById(long itemId) {
        return storage.findById(itemId)
                .map(itemMapper::toItemDto);
    }

    public Collection<ItemDto> userItems(String userId) {
        Long val = Long.parseLong(userId);
        return storage.userItems(val).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public Optional<Item> removeItem(Long id) {
        return storage.removeItem(id);
    }

    public Item create(String userId, ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);

        Long val = Long.parseLong(userId);

        Optional<User> optUsr = userStorage.findById(val);

        optUsr.orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%s не найден", userId)));

        return storage.create(val, item);
    }

    public Item update(String userId, String itemId, ItemDto itemDto) {
        Long usrId = Long.parseLong(userId);
        Long itmId = Long.parseLong(itemId);

        Optional<User> optUsr = userStorage.findById(usrId);

        optUsr.orElseThrow(() -> new NotFoundException(String.format("Пользователь с id=%s не найден", userId)));

        return storage.update(usrId, itmId, itemDto);
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
