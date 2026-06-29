package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService service;

    @Test
    void userItems_shouldReturnItems() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Drill");

        when(service.userItems("1")).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Drill"));

        verify(service).userItems("1");
    }

    @Test
    void findById_shouldReturnItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Drill");

        when(service.findById("1")).thenReturn(itemDto);

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Drill"));

        verify(service).findById("1");
    }

    @Test
    void create_shouldReturnCreatedItem() throws Exception {
        ItemDto request = new ItemDto();
        request.setName("Drill");
        request.setDescription("Power drill");
        request.setAvailable(true);

        ItemDto response = new ItemDto();
        response.setId(1L);
        response.setName("Drill");
        response.setDescription("Power drill");
        response.setAvailable(true);

        when(service.create(eq("1"), any(ItemDto.class))).thenReturn(response);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Drill"))
                .andExpect(jsonPath("$.description").value("Power drill"))
                .andExpect(jsonPath("$.available").value(true));

        verify(service).create(eq("1"), any(ItemDto.class));
    }

    @Test
    void update_shouldReturnUpdatedItem() throws Exception {
        ItemDto request = new ItemDto();
        request.setName("Updated Drill");
        request.setDescription("Updated description");
        request.setAvailable(true);

        ItemDto response = new ItemDto();
        response.setId(1L);
        response.setName("Updated Drill");
        response.setDescription("Updated description");
        response.setAvailable(true);

        when(service.update(eq("1"), eq("1"), any(ItemDto.class))).thenReturn(response);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Drill"));

        verify(service).update(eq("1"), eq("1"), any(ItemDto.class));
    }

    @Test
    void removerItem_shouldReturnOk_whenItemExists() throws Exception {
        ItemDto response = new ItemDto();
        response.setId(1L);
        response.setName("Drill");

        when(service.removeItem(1L)).thenReturn(response);

        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Drill"));

        verify(service).removeItem(1L);
    }

    @Test
    void removerItem_shouldReturnNotFound_whenItemNotExists() throws Exception {
        when(service.removeItem(1L)).thenReturn(null);

        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isNotFound());

        verify(service).removeItem(1L);
    }

    @Test
    void search_shouldReturnItems() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Drill");

        when(service.search("drill")).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "drill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Drill"));

        verify(service).search("drill");
    }

    @Test
    void createComment_shouldReturnComment() throws Exception {
        CommentDto request = new CommentDto();
        request.setText("Nice item");

        CommentDto response = new CommentDto();
        response.setId(1L);
        response.setText("Nice item");

        when(service.createComment(eq("1"), eq("1"), any(CommentDto.class))).thenReturn(response);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Nice item"));

        verify(service).createComment(eq("1"), eq("1"), any(CommentDto.class));
    }
}