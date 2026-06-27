package ru.practicum.shareit.request.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.RecordNotValidException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component("dbItemRequestStorage")
public class DbItemRequestStorage implements ItemRequestStorage {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    public ItemRequest create(Long userId, ItemRequest itemRequest) {
        LocalDateTime now = LocalDateTime.now();

        if (itemRequest.validateErrors().size() > 0) {
            String str = itemRequest.validateErrors()
                    .stream()
                    .collect(Collectors.joining(","));

            throw new RecordNotValidException(str);
        }
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        itemRequest.setCreated(now);
        itemRequest.setRequestor(requestor);
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequest findById(Long requestId) {
        return itemRequestRepository.findWithEagerRelationshipsById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id=%s не найден", requestId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequest> getItemRequestorsByRequestor(Long requestorId) {
        return itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequest> findAllItemRequests() {
        return itemRequestRepository.findAll();
    }
}
