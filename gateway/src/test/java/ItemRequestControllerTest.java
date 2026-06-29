package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Test
    @DisplayName("Создание запроса")
    void createShouldReturnOk() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need drill");

        when(itemRequestClient.create(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(itemRequestClient).create(eq(1L), any(ItemRequestDto.class));
    }

    @Test
    @DisplayName("Получение своих запросов")
    void getUserRequestsShouldReturnOk() throws Exception {
        when(itemRequestClient.getUserRequests(1L))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());

        verify(itemRequestClient).getUserRequests(1L);
    }

    @Test
    @DisplayName("Получение всех запросов с пагинацией")
    void findAllShouldReturnOk() throws Exception {
        when(itemRequestClient.findAll(1L, 0, 10))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(itemRequestClient).findAll(1L, 0, 10);
    }

    @Test
    @DisplayName("Получение запроса по id")
    void findByIdShouldReturnOk() throws Exception {
        when(itemRequestClient.findById(1L, 5L))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/requests/5")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk());

        verify(itemRequestClient).findById(1L, 5L);
    }
}