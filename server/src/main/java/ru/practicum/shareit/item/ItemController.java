package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @GetMapping
    public Collection<ItemDto> userItems(@RequestHeader("X-Sharer-User-Id") String userId) {
        return service.userItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable String itemId) {
        return service.findById(itemId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") String userId, @Valid @RequestBody ItemDto itemDto) {
        ItemDto createdItem = service.create(userId, itemDto);
        log.info("Создан пользователь с именем {}.", createdItem.getName());
        return createdItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable String itemId, @Valid @RequestBody ItemDto itemDto) {
        ItemDto oldItem = service.update(userId, itemId, itemDto);
        log.info("Обновлёна вещь с идентификатором {}.", itemId);
        return oldItem;
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ItemDto> removerItem(@PathVariable Long itemId) {
        ItemDto itemDto = service.removeItem(itemId);

        if (itemDto != null) {
            return ResponseEntity.ok(itemDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(name = "text") String text) {
        return service.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable String itemId, @Valid @RequestBody CommentDto commentDto) {
        return service.createComment(userId, itemId, commentDto);
    }
}
