package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;

    public ItemDto(String name, String description, Boolean available, Long requestId){
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}
