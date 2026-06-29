package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {

        UserDto userDto;
        if (item.getRequest() != null && item.getRequest().getRequestor() != null) {
            User requestor = item.getRequest().getRequestor();

            userDto = new UserDto(
                    requestor.getId(),
                    requestor.getName(),
                    requestor.getEmail()
            );
        } else {
            userDto = null;
        }

        ItemRequestDto itemRequestDto;

        if (item.getRequest() != null) {
            ItemRequest itemRequest = item.getRequest();

            List<ItemShortDto> items = new ArrayList<>();

            itemRequestDto = new ItemRequestDto(
                    item.getRequest().getId(),
                    item.getRequest().getDescription(),
                    item.getRequest().getRequestor().getId() != null ? item.getRequest().getRequestor().getId() : null,
                    userDto,
                    item.getRequest().getCreated(),
                    items
            );
        } else {
            itemRequestDto = null;
        }

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                item.getOwner() != null ? item.getOwner().getId() : null,
                itemRequestDto
        );
    }

    public Item toItem(ItemDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());

        if (dto.getRequest() != null) {
            ItemRequest request = new ItemRequest();
            request.setId(dto.getRequest().getId());
            request.setDescription(dto.getRequest().getDescription());
            request.setCreated(dto.getRequest().getCreated());
            item.setRequest(request);
        } else {
            item.setRequest(null);
        }

        return item;
    }
}
