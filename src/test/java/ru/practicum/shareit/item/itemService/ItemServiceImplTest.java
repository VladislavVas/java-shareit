package ru.practicum.shareit.item.itemService;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ItemServiceImpl itemService;

    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private UserDto userDto;

    @BeforeEach
    void getEntities() {
        itemDto1 = ItemDto.builder()
                .id(1L)
                .name("itemDto_1_name")
                .description("itemDto_1_description")
                .available(true)
                .requestId(null)
                .build();
        itemDto2 = ItemDto.builder()
                .name("itemDto_2_name")
                .description("itemDto_2_description")
                .available(true)
                .requestId(null)
                .build();
        userDto = UserDto.builder()
                .email("test@test.ru")
                .name("testName")
                .build();
        userService.addUser(userDto);
    }


    @Test
    void getAllItems() {
        itemService.addNewItem(1, itemDto1);
        itemService.addNewItem(1, itemDto2);
        assertEquals(2, itemService.getAllItems(1, 0,20).size());
    }


    @Test
    void getItem() {
        itemService.addNewItem(1, itemDto1);
        ItemDtoForRequest itemDto = itemService.getItem(1,1);
        assertEquals(1, itemDto.getId());
        assertEquals("itemDto_1_name", itemDto.getName());
    }

    @Test
    void getItemWrongId() {
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> itemService.getItem(1, 5));
        assertThat(e.getMessage()).contains(
                String.format("Вещь с id= " + 5 + " не существует"));
    }


    @Test
    void addNewItem() {
        itemService.addNewItem(1, itemDto1);
        ItemDtoForRequest itemDto = itemService.getItem(1,1);
        assertEquals(1, itemService.getAllItems(1,0,20).size());
        assertEquals(1,itemDto.getId());
    }

    @Test
    void updateItem() {

    }

    @Test
    void deleteItem() {
        itemService.addNewItem(1, itemDto1);
        assertEquals(1, itemService.getAllItems(1,0,20).size());
        itemService.deleteItem(1,1);
        assertEquals(0,itemService.getAllItems(1,0,20).size());
    }

    @Test
    void searchItemByText() {
        itemService.addNewItem(1, itemDto1);
        List<ItemDto> itemDto = itemService.searchItemByText("itemDto_1_name", 0, 20);
        assertEquals(1, itemDto.get(0).getId());
    }
}