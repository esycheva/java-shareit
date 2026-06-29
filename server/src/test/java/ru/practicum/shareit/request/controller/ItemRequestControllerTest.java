package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService service;

    @Test
    void create_shouldReturnCreatedRequest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");

        ItemRequestDto createdDto = new ItemRequestDto();
        createdDto.setId(1L);
        createdDto.setDescription("Need a drill");

        when(service.create(eq("1"), any(ItemRequestDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }

    @Test
    void getAllByItemRequests_shouldReturnList() throws Exception {
        ItemRequestDto dto1 = new ItemRequestDto();
        dto1.setId(1L);
        dto1.setDescription("Need a drill");

        ItemRequestDto dto2 = new ItemRequestDto();
        dto2.setId(2L);
        dto2.setDescription("Need a saw");

        when(service.getItemRequestsByRequestor("1")).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void findAllItemRequests_shouldReturnAllRequests() throws Exception {
        ItemRequestDto dto1 = new ItemRequestDto();
        dto1.setId(1L);
        dto1.setDescription("Need a drill");

        when(service.findAllItemRequests()).thenReturn(List.of(dto1));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Need a drill"));
    }

    @Test
    void findById_shouldReturnRequest() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(1L);
        dto.setDescription("Need a drill");

        when(service.findById("1", "1")).thenReturn(dto);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a drill"));
    }
}
