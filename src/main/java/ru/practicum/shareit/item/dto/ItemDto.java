package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;

    public ItemDto(Long id, String name, String description, Boolean available, Long requestId, Long ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
        this.ownerId = ownerId;
    }
}
