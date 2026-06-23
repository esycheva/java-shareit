package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    public BookingDto create(String userId, BookingDto bookingDto);

    public BookingDto approved(String userId, String bookingId, String flag);

    public Optional<Booking> find(Long id);

    public BookingDto findById(String userId, long bookingId);

    public List<BookingDto> getBookingsByBooker(Long bookerId, String state);

    public List<BookingDto> getAllByOwner(Long userId, String state);
}
