package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testItemRequestDtoSerialization() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Description");
        itemRequestDto.setCreated(now);
        itemRequestDto.setItems(Collections.emptyList());

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEmpty();
    }

    @Test
    void testItemRequestDtoDeserialization() throws Exception {
        String content = "{\"id\":1, \"description\":\"Description\", \"created\":\"2023-10-10T12:00:00\"}";

        ItemRequestDto result = json.parse(content).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Description");
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2023, 10, 10, 12, 0, 0));
    }
}
