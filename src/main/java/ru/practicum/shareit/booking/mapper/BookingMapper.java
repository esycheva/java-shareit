package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemShortMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final ItemShortMapper itemShortMapper;

    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        Item item = booking.getItem();

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
        if (booking.getItem() != null) {
            itemDto = new ItemDto(
                    booking.getItem().getId(),
                    booking.getItem().getName(),
                    booking.getItem().getDescription(),
                    booking.getItem().getAvailable(),
                    booking.getItem().getRequest() != null ? booking.getItem().getRequest().getId() : null,
                    booking.getItem().getOwner() != null ? booking.getItem().getOwner().getId() : null,
                    itemRequestDto
            );
        } else {
            itemDto = null;
        }

        UserDto bookerDto;
        if (booking.getBooker() != null) {
            bookerDto = new UserDto(
                    booking.getBooker().getId(),
                    booking.getBooker().getName(),
                    booking.getBooker().getEmail()
            );
        } else {
            bookerDto = null;
        }

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem() != null ? booking.getItem().getId() : null,
                booking.getBooker() != null ? booking.getBooker().getId() : null,
                booking.getStatus(),
                itemDto,
                bookerDto
        );
    }

    public Booking toBooking(BookingDto dto) {
        if (dto == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setStatus(dto.getStatus());
        return booking;
    }
}