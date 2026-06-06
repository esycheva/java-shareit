package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private Long id;
    private String name;
    // поле уникально для пользователя
    @Email
    private String email;

    public List<String> validateErrors() {
        List<String> errors = new ArrayList<>();

        if (email == null || email.isBlank() || email.isEmpty()) {
            errors.add("Электронная почта не может быть пустой.");
        }

        if (email != null && !email.contains("@")) {
            errors.add("Электронная почта должна содержать @.");
        }

        if (name == null || name.isBlank() || name.isEmpty()) {
            errors.add("Имя не может быть пустым.");
        }

        return errors;
    }
}
