package ru.practicum.shareit.request.integration;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.datasource.url=jdbc:h2:mem:shareit"
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceIntegrationTest {

    private final EntityManager em;
    private final ItemRequestService service;

    @Test
    void saveItemRequest_shouldReturnDtoAndPersistInDb() {
        User user = makeUser("Ivan", "ivan@mail.ru");
        em.persist(user);

        ItemRequestDto inputDto = new ItemRequestDto();
        inputDto.setDescription("Need a hammer for home repair");

        ItemRequestDto result = service.create(String.valueOf(user.getId()), inputDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getDescription(), equalTo(inputDto.getDescription()));
        assertThat(result.getCreated(), notNullValue());

        ItemRequest savedRequest = em.createQuery(
                        "Select ir from ItemRequest ir where ir.id = :id", ItemRequest.class)
                .setParameter("id", result.getId())
                .getSingleResult();

        assertThat(savedRequest.getDescription(), equalTo(inputDto.getDescription()));
        assertThat(savedRequest.getRequestor().getId(), equalTo(user.getId()));
    }

    private User makeUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    private ItemRequest makeRequest(String description, User requestor) {
        ItemRequest request = new ItemRequest();
        request.setDescription(description);
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());
        return request;
    }
}
