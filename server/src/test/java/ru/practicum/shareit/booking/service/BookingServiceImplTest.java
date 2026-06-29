package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exceptions.RecordNotValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingStorage storage;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        User owner = new User();
        owner.setId(2L);

        item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(owner);

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);

        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
    }

    @Test
    void create_whenValid_thenSaveBooking() {
        when(userStorage.find(1L)).thenReturn(Optional.of(user));
        when(itemStorage.find(1L)).thenReturn(Optional.of(item));
        when(bookingMapper.toBooking(any())).thenReturn(booking);
        when(storage.create(eq(1L), any())).thenReturn(booking);
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        BookingDto result = bookingService.create("1", bookingDto);

        assertNotNull(result);
        verify(storage).create(eq(1L), any());
    }

    @Test
    void create_whenItemNotAvailable_thenThrowException() {
        item.setAvailable(false);
        when(userStorage.find(1L)).thenReturn(Optional.of(user));
        when(itemStorage.find(1L)).thenReturn(Optional.of(item));
        when(bookingMapper.toBooking(any())).thenReturn(booking);

        assertThrows(RecordNotValidException.class, () -> bookingService.create("1", bookingDto));
    }

    @Test
    void approved_whenUserIsNotOwner_thenThrowException() {
        when(storage.find(1L)).thenReturn(Optional.of(booking));

        assertThrows(RuntimeException.class, () ->
                bookingService.approved("1", "1", "true")
        );
    }

    @Test
    void findById_whenUserHasNoAccess_thenThrowException() {
        when(storage.findById(1L)).thenReturn(booking);

        assertThrows(RuntimeException.class, () ->
                bookingService.findById("3", 1L)
        );
    }
}
