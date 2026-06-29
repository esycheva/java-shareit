package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingDtoSerialization() throws Exception {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 2, 10, 0);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(2L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);

        assertThat(result).extractingJsonPathStringValue("$.start").contains("2024-01-01T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").contains("2024-01-02T10:00:00");
    }

    @Test
    void testBookingDtoDeserialization() throws Exception {
        String content = "{\"id\":10, \"itemId\":5, \"start\":\"2024-12-01T12:00:00\", \"end\":\"2024-12-02T12:00:00\"}";

        BookingDto bookingDto = json.parse(content).getObject();

        assertThat(bookingDto.getId()).isEqualTo(10L);
        assertThat(bookingDto.getItemId()).isEqualTo(5L);
        assertThat(bookingDto.getStart()).isEqualTo(LocalDateTime.of(2024, 12, 1, 12, 0));
        assertThat(bookingDto.getEnd()).isEqualTo(LocalDateTime.of(2024, 12, 2, 12, 0));
    }
}