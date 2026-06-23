package ru.practicum.shareit.item.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.RecordNotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component("dbItemStorage")
public class DbItemStorage implements ItemStorage {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public List<Item> userItems(Long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    public Item create(Long userId, Item item) {
        if (item.validateErrors().size() > 0) {
            String str = item.validateErrors()
                    .stream()
                    .collect(Collectors.joining(","));

            throw new RecordNotValidException(str);
        }
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        item.setOwner(owner);
        return itemRepository.save(item);
    }

    public Comment createComment(Long userId, Long itemId, Comment comment) {
        if (comment.validateErrors().size() > 0) {
            String str = comment.validateErrors()
                    .stream()
                    .collect(Collectors.joining(","));

            throw new RecordNotValidException(str);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        boolean wasBooked = bookingRepository
                .existsByBookerIdAndItemIdAndEndBeforeAndStatus(
                        author.getId(),
                        item.getId(),
                        LocalDateTime.now(),
                        Status.APPROVED
                );

        if (!wasBooked) {
            throw new RecordNotValidException("Пользователь не брал эту вещь в аренду");
        }
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setAuthorName(author.getName());
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Item update(Long userId, Long itemId, ItemDto itemDto) {
        if (userId.equals(null)) {
            throw new RecordNotValidException("UserId должен быть указан.");
        }

        if (itemId.equals(null)) {
            throw new RecordNotValidException("Id должен быть указан.");
        }

        Item oldItem = find(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%s не найден.", itemId)));

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        oldItem.setOwner(owner);

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            oldItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            oldItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        return itemRepository.save(oldItem);
    };

    public Item removeItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException(String.format("Вещь с id=%s не найдена", itemId)));

        itemRepository.delete(item);
        return item;
    };

    @Override
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id=%s не найдена", itemId)));
    };

    @Override
    public Optional<Item> find(Long id) {
        return itemRepository.findById(id);
    };

    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.searchAvailableItems(text);
    }

    public List<Comment> findComments(Long itemId) {
        return commentRepository.findAllByItemId(itemId);
    }

    public List<Comment> findCommentsByItemIds(List<Long> itemIds) {
        return commentRepository.findAllByItemIdIn(itemIds);
    }
}
