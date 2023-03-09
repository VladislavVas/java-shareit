package ru.practicum.shareit.requests.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemRequestStorageTest {

    @Autowired
    private ItemRequestStorage itemRequestStorage;
    @Autowired
    private UserStorage userStorage;

    private User user;
    private ItemRequest itemRequest;
    private List<ItemRequest> itemRequestList;


    @BeforeEach
    void getEntities() {
        user = User.builder()
                .name("testName")
                .email("test@test.ru")
                .build();
        itemRequest = ItemRequest.builder()
                .requester(user)
                .description("description")
                .items(Collections.emptyList())
                .date(LocalDateTime.now())
                .build();
        userStorage.save(user);
        itemRequestStorage.save(itemRequest);
    }

    @Test
    void findAllByRequesterNotEquals() {
        itemRequestList = itemRequestStorage.findAllByRequesterIdNotEquals(user.getId(), PageRequest.of(0, 20));
        assertThat(itemRequestList).isEmpty();
    }

    @Test
    void findAllByRequester() {
        itemRequestList = itemRequestStorage.findAllByRequester(user, Sort.by("date"));
        assertThat(itemRequestList).hasSize(1).contains(itemRequest);
    }
}