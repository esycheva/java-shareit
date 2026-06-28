package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime created;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    public List<String> validateErrors() {
        List<String> errors = new ArrayList<>();

        if (description == null || description.isBlank() || description.isEmpty()) {
            errors.add("Описание не может быть пустым.");
        }

        return errors;
    }
}
