package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class CommentMapper {
    private final ItemShortMapper itemShortMapper;

    public CommentDto toCommentDto(Comment comment) {

        Item item = comment.getItem();

        UserDto requestorDto;
        if (item.getRequest() != null && item.getRequest().getRequestor() != null) {
            User requestor = item.getRequest().getRequestor();

            requestorDto = new UserDto(
                    requestor.getId(),
                    requestor.getName(),
                    requestor.getEmail()
            );
        } else {
            requestorDto = null;
        }

        ItemRequestDto itemRequestDto;
        if (item.getRequest() != null) {
            ItemRequest itemRequest = item.getRequest();

            List<ItemShortDto> items = new ArrayList<>();

            if (itemRequest.getItems() != null) {
                items = itemRequest.getItems().stream()
                        .map(itemShortMapper::toItemShortDto)
                        .toList();
            }

            itemRequestDto = new ItemRequestDto(
                    item.getRequest().getId(),
                    item.getRequest().getDescription(),
                    item.getRequest().getRequestor() != null ? item.getRequest().getRequestor().getId() : null,
                    requestorDto,
                    item.getRequest().getCreated(),
                    items
            );
        } else {
            itemRequestDto = null;
        }


        ItemDto itemDto;
        if (item != null) {
            itemDto = new ItemDto(
                    comment.getItem().getId(),
                    comment.getItem().getName(),
                    comment.getItem().getDescription(),
                    comment.getItem().getAvailable(),
                    comment.getItem().getRequest() != null ? comment.getItem().getRequest().getId() : null,
                    comment.getItem().getOwner() != null ? comment.getItem().getOwner().getId() : null,
                    itemRequestDto
            );
        } else {
            itemDto = null;
        }

        UserDto userDto;
        if (comment.getAuthor() != null) {
            userDto = new UserDto(
                    comment.getAuthor().getId(),
                    comment.getAuthor().getName(),
                    comment.getAuthor().getEmail()
            );
        } else {
            userDto = null;
        }

        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthorName(),
                comment.getCreated(),
                comment.getItem() != null ? comment.getItem().getId() : null,
                comment.getAuthor() != null ? comment.getAuthor().getId() : null,
                itemDto,
                userDto
        );
    }

    public Comment toComment(CommentDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        return comment;
    }
}
