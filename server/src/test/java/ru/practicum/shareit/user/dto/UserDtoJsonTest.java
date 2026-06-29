package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    @DisplayName("Проверка сериализации UserDto")
    void testSerialize() throws Exception {
        UserDto userDto = new UserDto(1L, "Ivan", "ivan@mail.com");

        assertThat(json.write(userDto)).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json.write(userDto)).extractingJsonPathStringValue("$.name").isEqualTo("Ivan");
        assertThat(json.write(userDto)).extractingJsonPathStringValue("$.email").isEqualTo("ivan@mail.com");
    }

    @Test
    @DisplayName("Проверка десериализации UserDto")
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Ivan\",\"email\":\"ivan@mail.com\"}";

        UserDto userDto = json.parseObject(content);

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("Ivan");
        assertThat(userDto.getEmail()).isEqualTo("ivan@mail.com");
    }
}