package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Email
    @Column(name = "email", nullable = false)
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
