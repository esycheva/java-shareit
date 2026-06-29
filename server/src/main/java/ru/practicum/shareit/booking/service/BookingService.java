package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingDto create(String userId, BookingDto bookingDto);

    BookingDto approved(String userId, String bookingId, String flag);

    Optional<Booking> find(Long id);

    BookingDto findById(String userId, long bookingId);

    List<BookingDto> getBookingsByBooker(Long bookerId, String state);

    List<BookingDto> getAllByOwner(Long userId, String state);
}
