package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemShortMapper {
    public ItemShortDto toItemShortDto(Item item) {

        return new ItemShortDto(
                item.getId(),
                item.getName(),
                item.getOwner() != null ? item.getOwner().getId() : null
        );
    }

    public Item toItem(ItemShortDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        return item;
    }
}
