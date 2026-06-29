package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService service;

    @Test
    void create_shouldReturnCreatedBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(2L);

        when(service.create(eq("1"), any(BookingDto.class))).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemId").value(2));

        verify(service).create(eq("1"), any(BookingDto.class));
    }

    @Test
    void createComment_shouldReturnCreatedBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(2L);

        when(service.create(eq("1"), any(BookingDto.class))).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings/items/2/comment")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemId").value(2));

        verify(service).create(eq("1"), any(BookingDto.class));
    }

    @Test
    void approve_shouldReturnUpdatedBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(2L);

        when(service.approved("1", "1", "true")).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemId").value(2));

        verify(service).approved("1", "1", "true");
    }

    @Test
    void findById_shouldReturnBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(2L);

        when(service.findById("1", 1L)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemId").value(2));

        verify(service).findById("1", 1L);
    }

    @Test
    void getAllByBooker_shouldReturnBookings() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(2L);

        when(service.getBookingsByBooker(1L, "ALL")).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].itemId").value(2));

        verify(service).getBookingsByBooker(1L, "ALL");
    }

    @Test
    void getAllByOwner_shouldReturnBookings() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(2L);

        when(service.getAllByOwner(1L, "ALL")).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].itemId").value(2));

        verify(service).getAllByOwner(1L, "ALL");
    }
}