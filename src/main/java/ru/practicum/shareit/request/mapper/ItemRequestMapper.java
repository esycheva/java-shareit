package ru.practicum.shareit.request.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemShortMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ItemRequestMapper {
    private final ItemShortMapper itemShortMapper;

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {

        UserDto userDto;
        if (itemRequest.getRequestor() != null) {
            userDto = new UserDto(
                    itemRequest.getRequestor().getId(),
                    itemRequest.getRequestor().getName(),
                    itemRequest.getRequestor().getEmail()
            );
        } else {
            userDto = null;
        }

        List<ItemShortDto> items = new ArrayList<>();

        if (itemRequest.getItems() != null) {
            items = itemRequest.getItems().stream()
                    .map(itemShortMapper::toItemShortDto)
                    .toList();
        }

        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestor() != null ? itemRequest.getRequestor().getId() : null,
                userDto,
                itemRequest.getCreated(),
                items
        );
    }

    public ItemRequest toItemRequest(ItemRequestDto dto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setCreated(dto.getCreated());
        return itemRequest;
    }
}
