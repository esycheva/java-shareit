package ru.practicum.shareit.item.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;

    public List<String> validateErrors() {
        List<String> errors = new ArrayList<>();

        if (name == null || name.isBlank() || name.isEmpty()) {
            errors.add("Наименование не может быть пустым.");
        }

        if (description == null || description.isBlank() || description.isEmpty()) {
            errors.add("Описание не может быть пустым.");
        }

        if (available == null) {
            errors.add("Описание не может быть пустым.");
        }

        return errors;
    }
}
