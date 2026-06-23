package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.RecordNotValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingStorage storage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(@Qualifier("dbBookingStorage") BookingStorage storage, @Qualifier("dbItemStorage") ItemStorage itemStorage, @Qualifier("dbUserStorage") UserStorage userStorage, BookingMapper bookingMapper) {
        this.storage = storage;
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.bookingMapper = bookingMapper;
    }

    public BookingDto create(String userId, BookingDto bookingDto) {
        Long userIdL = Long.parseLong(userId);

        Booking booking = bookingMapper.toBooking(bookingDto);

        Item item = findItem(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Вещь с идентификатором id=%s не найдена", bookingDto.getItemId())
                ));

        User user = findUser(userIdL)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с идентификатором id=%s не найден", userIdL)
                ));

        if (!item.getAvailable()) {
            throw new RecordNotValidException(
                    String.format("Вещь недоступна для бронирования is_available=%s", item.getAvailable())
            );
        }

        booking.setItem(item);
        booking.setBooker(user);

        Booking createdBooking = storage.create(userIdL, booking);
        return bookingMapper.toBookingDto(createdBooking);
    }

    public BookingDto approved(String userId, String bookingId, String flag) {
        Long userIdL;
        try {
            userIdL = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new NotFoundException("Некорректный user_id: " + userId);
        }

        Long bookingIdL;
        try {
            bookingIdL = Long.parseLong(bookingId);
        } catch (NumberFormatException e) {
            throw new NotFoundException("Некорректный booking_id: " + bookingId);
        }

        boolean approved = Boolean.parseBoolean(flag);

        Booking booking = find(bookingIdL)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Бронирование с идентификатором id=%s не найдена", bookingId)));

        if (booking.getItem() == null) {
            throw new NotFoundException("К бронированию не привязана вещь");
        }

        if (!booking.getItem().getOwner().getId().equals(userIdL)) {
            throw new RuntimeException(String.format(
                    "Пользователь с id=%s не является владельцем вещи и не может менять статус", userId));
        }

        Booking updatedBooking = storage.approve(bookingIdL, approved);

        return bookingMapper.toBookingDto(updatedBooking);
    }

    public BookingDto findById(String userId, long bookingId) {
        Long userIdL = Long.parseLong(userId);

        Booking booking = storage.findById(bookingId);

        Item item = booking.getItem();
        User booker = booking.getBooker();

        Boolean flag = false;

        if (item.getOwner().getId().equals(userIdL)) {
            flag = true;
        }

        if (booker.getId().equals(userIdL)) {
            flag = true;
        }

        if (!flag) {
            throw new RuntimeException(
                    String.format("Пользователь с id=%s не является ни владельцем ни бронирующим", userIdL)
            );
        }

        return bookingMapper.toBookingDto(booking);
    }

    public Optional<Booking> find(Long id) {
        return storage.find(id);
    }

    public Optional<Item> findItem(Long id) {
        return itemStorage.find(id);
    }

    public Optional<User> findUser(Long id) {
        return userStorage.find(id);
    }

    public List<BookingDto> getBookingsByBooker(Long bookerId, String state) {
        List<Booking> bookings = storage.getBookingsByBooker(bookerId, state);

        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getAllByOwner(Long userId, String state) {
        List<Booking> bookings = storage.getAllByOwner(userId, state);
        return bookings.stream()
                .map(bookingMapper::toBookingDto)
                .toList();
    }


}
