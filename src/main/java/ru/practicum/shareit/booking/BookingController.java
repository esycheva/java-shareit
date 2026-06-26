package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") String userId, @Valid @RequestBody BookingDto bookingDto) {
        BookingDto createdBooking = service.create(userId, bookingDto);
        log.info("Создано бронирование с именем {}.", createdBooking.getId());
        return createdBooking;
    }

    @PostMapping("/items/{itemId}/comment")
    public BookingDto createComment(@RequestHeader("X-Sharer-User-Id") String userId, @Valid @RequestBody BookingDto bookingDto) {
        BookingDto createdBooking = service.create(userId, bookingDto);
        log.info("Создано бронирование с именем {}.", createdBooking.getId());
        return createdBooking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable String bookingId, @RequestParam(name = "approved") String approved) {
        System.out.println(userId);
        System.out.println(bookingId);
        System.out.println(approved);
        BookingDto oldBooking = service.approved(userId, bookingId, approved);
        log.info("Обновлено бронирование с идентификатором {}.", bookingId);
        return oldBooking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") String userId, @PathVariable long bookingId) {
        return service.findById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return service.getBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return service.getAllByOwner(userId, state);
    }
}
