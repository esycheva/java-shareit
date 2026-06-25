package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "items", schema = "public")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "request_id")
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
