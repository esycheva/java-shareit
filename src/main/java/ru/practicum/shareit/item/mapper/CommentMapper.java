package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {

        ItemDto itemDto;
        if (comment.getItem() != null) {
            itemDto = new ItemDto(
                    comment.getItem().getId(),
                    comment.getItem().getName(),
                    comment.getItem().getDescription(),
                    comment.getItem().getAvailable(),
                    comment.getItem().getRequestId() != null ? comment.getItem().getRequestId() : null,
                    comment.getItem().getOwner().getId() != null ? comment.getItem().getOwner().getId() : null
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
