package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    private static final Logger log = LoggerFactory.getLogger(ItemRequestController.class);

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") String userId, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequestDto createdRequest = service.create(userId, itemRequestDto);
        log.info("Запрос вещи с описание {}.", createdRequest.getDescription());
        return createdRequest;
    }

    // GET /requests
    // получить список своих запросов вместе с данными об ответах на них
    @GetMapping
    public List<ItemRequestDto> getAllByItemRequests(
            @RequestHeader("X-Sharer-User-Id") String userId) {
        return service.getItemRequestsByRequestor(userId);
    }

    // GET /requests/all
    // запросы других пользователей
    @GetMapping("/all")
    public Collection<ItemRequestDto> findAllItemRequests() {
        return service.findAllItemRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable String requestId) {
        return service.findById(userId, requestId);
    }

    // Добавьте поле requestId в тело запроса POST /items.
    // Обратите внимание: должна сохраниться возможность добавить вещь и без указания requestId.
}
