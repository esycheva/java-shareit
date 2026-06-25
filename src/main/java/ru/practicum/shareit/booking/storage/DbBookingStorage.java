package ru.practicum.shareit.booking.storage;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.RecordNotValidException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("dbBookingStorage")
public class DbBookingStorage implements BookingStorage {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public DbBookingStorage(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Booking create(Long userId, Booking booking) {
        if (booking.validateErrors().size() > 0) {
            String str = booking.validateErrors()
                    .stream()
                    .collect(Collectors.joining(","));

            throw new RecordNotValidException(str);
        }
        booking.setStatus(Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking approve(Long bookingId, Boolean flag) {
        Optional<Booking> bookingOpt = find(bookingId);
        Booking booking = bookingOpt.get();

        if (flag) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        Booking savedBooking = bookingRepository.saveAndFlush(booking);

        Booking bookingWithRelations = bookingRepository.findWithEagerRelationshipsById(savedBooking.getId())
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено сразу после сохранения"));

        return bookingWithRelations;
    }

    @Override
    public Optional<Booking> find(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Booking findById(Long bookingId) {
        return bookingRepository.findWithEagerRelationshipsById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
    }

    public List<Booking> getBookingsByBooker(Long bookerId, String state) {
        BookingState bookingState = BookingState.valueOf(state == null ? "ALL" : state.toUpperCase());
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings;

        switch (bookingState) {
            case ALL -> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
            case CURRENT -> bookings = bookingRepository.findCurrentByBookerId(bookerId, now);
            case PAST -> bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now);
            case FUTURE -> bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, now);
            case WAITING -> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, Status.WAITING);
            case REJECTED -> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, Status.REJECTED);
            default -> throw new IllegalArgumentException("Unknown state: " + state);
        }
        return bookings;
    }

    public List<Booking> getAllByOwner(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (itemRepository.findAllByOwnerId(userId).isEmpty()) {
            throw new RecordNotValidException("У пользователя нет вещей для проверки бронирований");
        }

        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Unknown state: " + state);
        }

        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;

        switch (bookingState) {
            case ALL -> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case CURRENT -> bookings = bookingRepository.findCurrentByOwnerId(userId, now);
            case PAST -> bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING ->
                    bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED ->
                    bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            default -> throw new NotFoundException("Unknown state: " + state);
        }
        return bookings;
    }

    public Optional<Booking> lastBooking(Long itemId) {
        LocalDateTime now = LocalDateTime.now();

        return bookingRepository.findFirstByItemIdAndStatusAndEndBeforeOrderByEndDesc(itemId, Status.APPROVED, now);
    }

    public Optional<Booking> nextBooking(Long itemId) {
        LocalDateTime now = LocalDateTime.now();

        return bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(itemId, Status.APPROVED, now);
    }
}
