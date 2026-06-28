package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingStorage {

    Booking create(Long userId, Booking booking);

    Booking approve(Long bookingId, Boolean flag);

    Optional<Booking> find(Long id);

    Booking findById(Long userId);

    List<Booking> getBookingsByBooker(Long bookerId, String state);

    List<Booking> getAllByOwner(Long userId, String state);

    Optional<Booking> lastBooking(Long itemId);

    Optional<Booking> nextBooking(Long itemId);

}
