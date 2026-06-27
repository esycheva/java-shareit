package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.RecordNotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items;

    @Autowired
    public InMemoryItemStorage() {
        this.items = new HashMap<>();
    }

    public List<Item> userItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    public Item create(Long userId, Long requestId, Item item) {
        if (item.validateErrors().size() > 0) {
            String str = item.validateErrors()
                    .stream()
                    .collect(Collectors.joining(","));

            throw new RecordNotValidException(str);
        }
        User user = new User();
        user.setId(userId);
        item.setOwner(user);
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        if (userId.equals(null)) {
            throw new RecordNotValidException("UserId должен быть указан.");
        }

        if (itemId.equals(null)) {
            throw new RecordNotValidException("Id должен быть указан.");
        }

        if (!items.containsKey(itemId)) {
            throw new NotFoundException(String.format("Вещь с id=%s не найден.", itemId));
        }

        Item oldItem = items.get(itemId);

        User user = new User();
        user.setId(userId);
        oldItem.setOwner(user);

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            oldItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            oldItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }

        return oldItem;
    }

    public Item findById(Long itemId) {
        Optional<Item> optItem = find(itemId);
        optItem.orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%s не найден", itemId)));
        return optItem.get();
    }

    private Long getNextId() {
        Long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Optional<Item> find(Long id) {
        return items.values().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    public Item removeItem(Long itemId) {
        Optional<Item> optItem = find(itemId);
        optItem.orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%s не найден", itemId)));
        return optItem.get();
    }

    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String query = text.toLowerCase();

        return items.values().stream()
                .filter(item -> item.getAvailable())
                .filter(item -> item.getName().toLowerCase().contains(query)
                        || item.getDescription().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    public Comment createComment(Long userId, Long itemId, Comment comment) {
        return new Comment();
    }

    public List<Comment> findComments(Long itemId) {
        return new ArrayList<>();
    }

    public List<Comment> findCommentsByItemIds(List<Long> itemIds) {
        return new ArrayList<>();
    }
}
