package ru.practicum.shareit.booking.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntegrationTest {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    private UserDto booker;
    private ItemDto item;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setName("Booker");
        userDto.setEmail("booker@mail.com");
        booker = userService.create(userDto);

        UserDto ownerDto = new UserDto();
        ownerDto.setName("Owner");
        ownerDto.setEmail("owner@mail.com");
        UserDto owner = userService.create(ownerDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Hammer");
        itemDto.setDescription("Heavy hammer");
        itemDto.setAvailable(true);
        item = itemService.create(owner.getId().toString(), itemDto);
    }

    @Test
    void shouldReturnAllBookingsByBooker() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto created = bookingService.create(booker.getId().toString(), bookingDto);

        List<BookingDto> bookings = bookingService.getBookingsByBooker(booker.getId(), "ALL");

        assertThat(bookings, hasSize(1));
        assertThat(bookings.get(0).getId(), equalTo(created.getId()));
        assertThat(bookings.get(0).getItemId(), equalTo(item.getId()));
        assertThat(bookings.get(0).getStatus(), equalTo(Status.WAITING));
    }

    @Test
    void shouldApproveBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto created = bookingService.create(booker.getId().toString(), bookingDto);

        Long ownerId = itemService.findById(item.getId().toString()).getOwnerId();


        bookingService.approved(ownerId.toString(), created.getId().toString(), "true");

        BookingDto updated = bookingService.findById(booker.getId().toString(), created.getId());
        assertThat(updated.getStatus(), equalTo(Status.APPROVED));
    }
}
