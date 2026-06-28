package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestStorage {
    ItemRequest create(Long userId, ItemRequest itemRequest);

    ItemRequest findById(Long requestId);

    List<ItemRequest> getItemRequestorsByRequestor(Long requestorId);

    List<ItemRequest> findAllItemRequests();
}
