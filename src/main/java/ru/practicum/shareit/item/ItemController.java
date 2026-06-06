package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
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
    public Optional<ItemDto> findById(@PathVariable long itemId) {
        return service.findById(itemId);
    }

    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") String userId, @Valid @RequestBody ItemDto itemDto) {
        System.out.println("THIS IS HEADER " + userId);
        Item createdItem = service.create(userId, itemDto);
        log.info("Создан пользователь с именем {}.", createdItem.getName());
        return createdItem;
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable String itemId, @Valid @RequestBody ItemDto itemDto) {
        Item oldItem = service.update(userId, itemId, itemDto);
        log.info("Обновлёна вещь с идентификатором {}.", itemId);
        return oldItem;
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Item> removerItem(@PathVariable Long itemId) {
        Optional<Item> optItem = service.removeItem(itemId);

        return optItem.map(item -> ResponseEntity
                .status(HttpStatus.OK)
                .body(item)).orElseGet(() -> ResponseEntity
                .notFound().build());
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(name = "text") String text) {
        return service.search(text);
    }
}
