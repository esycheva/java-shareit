package ru.practicum.shareit.item.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {

    private final ItemService itemService;
    private final UserService userService;

    @Test
    void userItems_shouldReturnCorrectItemsWithFullFields() {
        // Создаем владельца
        UserDto ownerDto = new UserDto();
        ownerDto.setName("Owner");
        ownerDto.setEmail("owner@mail.com");
        UserDto savedOwner = userService.create(ownerDto);
        String ownerId = String.valueOf(savedOwner.getId());

        // Создаем вещи для владельца
        ItemDto item1 = new ItemDto();
        item1.setName("Hammer");
        item1.setDescription("Heavy hammer");
        item1.setAvailable(true);
        itemService.create(ownerId, item1);

        ItemDto item2 = new ItemDto();
        item2.setName("Screwdriver");
        item2.setDescription("Electric screwdriver");
        item2.setAvailable(false);
        itemService.create(ownerId, item2);

        // Создаем другого пользователя и его вещь (для проверки фильтрации)
        UserDto otherUserDto = new UserDto();
        otherUserDto.setName("Other");
        otherUserDto.setEmail("other@mail.com");
        UserDto savedOther = userService.create(otherUserDto);

        ItemDto otherItem = new ItemDto();
        otherItem.setName("Other item");
        otherItem.setDescription("Should not be in list");
        otherItem.setAvailable(true);
        itemService.create(String.valueOf(savedOther.getId()), otherItem);

        Collection<ItemDto> result = itemService.userItems(ownerId);

        assertThat(result, hasSize(2));

        assertThat(result, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo("Hammer")),
                hasProperty("description", equalTo("Heavy hammer")),
                hasProperty("available", is(true))
        )));

        assertThat(result, hasItem(allOf(
                hasProperty("name", equalTo("Screwdriver")),
                hasProperty("description", equalTo("Electric screwdriver")),
                hasProperty("available", is(false))
        )));

        assertThat(result, not(hasItem(hasProperty("name", equalTo("Other item")))));
    }
}
