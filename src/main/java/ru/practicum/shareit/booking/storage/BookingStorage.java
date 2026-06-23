package ru.practicum.shareit.booking.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingStorage {

    public Booking create(Long userId, Booking booking);

    public Booking approve(Long bookingId, Boolean flag);

    public Optional<Booking> find(Long id);

    public Booking findById(Long userId);

    public List<Booking> getBookingsByBooker(Long bookerId, String state);

    public List<Booking> getAllByOwner(Long userId, String state);

    public Optional<Booking> lastBooking(Long itemId);

    public Optional<Booking> nextBooking(Long itemId);

}
