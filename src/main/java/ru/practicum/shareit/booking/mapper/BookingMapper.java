package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class BookingMapper {

    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        ItemDto itemDto;
        if (booking.getItem() != null) {
            itemDto = new ItemDto(
                    booking.getItem().getId(),
                    booking.getItem().getName(),
                    booking.getItem().getDescription(),
                    booking.getItem().getAvailable(),
                    booking.getItem().getRequestId() != null ? booking.getItem().getRequestId() : null,
                    booking.getItem().getOwner().getId() != null ? booking.getItem().getOwner().getId() : null
            );
        } else {
            itemDto = null;
        }

        UserDto userDto;
        if (booking.getBooker() != null) {
            userDto = new UserDto(
                    booking.getBooker().getId(),
                    booking.getBooker().getName(),
                    booking.getBooker().getEmail()
            );
        } else {
            userDto = null;
        }

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem() != null ? booking.getItem().getId() : null,
                booking.getBooker() != null ? booking.getBooker().getId() : null,
                booking.getStatus(),
                itemDto,
                userDto
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