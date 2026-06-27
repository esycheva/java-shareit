package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestMapper itemRequestMapper;
    private final UserStorage userStorage;
    private final ItemRequestStorage storage;

    @Autowired
    public ItemRequestServiceImpl(@Qualifier("dbItemRequestStorage") ItemRequestStorage storage, @Qualifier("dbUserStorage") UserStorage userStorage, ItemRequestMapper itemRequestMapper) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.itemRequestMapper = itemRequestMapper;
    }

    public ItemRequestDto create(String userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);

        Long userIdL = Long.parseLong(userId);

        userStorage.findById(userIdL);

        ItemRequest createdItemRequest = storage.create(userIdL, itemRequest);

        return itemRequestMapper.toItemRequestDto(createdItemRequest);
    }

    public ItemRequestDto findById(String userId, String requestId) {
        Long userIdL = Long.parseLong(userId);
        Long requestIdL = Long.parseLong(requestId);

        ItemRequest itemRequest = storage.findById(requestIdL);

        return itemRequestMapper.toItemRequestDto(itemRequest);
    }


    public List<ItemRequestDto> getItemRequestsByRequestor(String requestorId) {
        Long requestorIdL = Long.parseLong(requestorId);

        List<ItemRequest> itemRequests = storage.getItemRequestorsByRequestor(requestorIdL);

        return itemRequests.stream()
                .map(itemRequestMapper::toItemRequestDto)
                .toList();
    }

    public Collection<ItemRequestDto> findAllItemRequests() {
        return storage.findAllItemRequests().stream()
                .map(itemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

}
