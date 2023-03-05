package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ItemStorageTest {
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;

    private User user1;
    private User user2;
    private Item item;

    @BeforeEach
    void getEntities() {
        user1 = User.builder()
                .name("testName")
                .email("test@test.ru")
                .build();
        user2 = User.builder()
                .name("wrongName")
                .email("wrong@test.ru").build();
        item = Item.builder()
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .owner(user1)
                .build();
        userStorage.save(user1);
        itemStorage.save(item);
        userStorage.save(user2);
    }

    @Test
    void getItemsByOwner() {
        List<Item> items = itemStorage.getItemsByOwner(user1, PageRequest.of(0, 20));
        assertThat(items).hasSize(1).contains(item);
        items = itemStorage.getItemsByOwner(user2, PageRequest.of(0, 20));
        assertThat(items).isEmpty();
    }

    @Test
    void searchItem() {
        List<Item> items = itemStorage.searchItem(PageRequest.of(0, 20), "wrongItem");
        assertThat(items).isEmpty();
        items = itemStorage.searchItem(PageRequest.of(0, 20), "itemName");
        assertThat(items).hasSize(1).contains(item);
    }
}