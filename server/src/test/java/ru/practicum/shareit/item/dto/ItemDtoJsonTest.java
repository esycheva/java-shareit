package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDtoSerialization() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);

        // Предположим, что у вас есть вложенные классы для комментариев
        CommentDto comment = new CommentDto();
        comment.setId(1L);
        comment.setText("Отличный инструмент");
        itemDto.setComments(List.of(comment));

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(1);
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("Отличный инструмент");
    }

    @Test
    void testItemDtoDeserialization() throws Exception {
        String content = "{\"id\":1,\"name\":\"Дрель\",\"description\":\"Мощная дрель\",\"available\":true}";

        ItemDto itemDto = json.parse(content).getObject();

        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Дрель");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getDescription()).isEqualTo("Мощная дрель");
    }
}