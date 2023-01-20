package ru.practicum.shareit;

//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.practicum.shareit.booking.controller.BookingController;
//import ru.practicum.shareit.booking.dto.BookingRequestDto;
//import ru.practicum.shareit.booking.model.Status;
//import ru.practicum.shareit.item.controller.ItemController;
//import ru.practicum.shareit.item.dto.CommentDto;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.user.controller.UserController;
//import ru.practicum.shareit.user.dto.UserDto;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShareItTests {
//
//    @Autowired
//    ItemController itemController;
//    @Autowired
//    UserController userController;
//    @Autowired
//    BookingController bookingController;
//
//    UserDto userDto = UserDto.builder()
//            .email("test@test.ru")
//            .name("testName")
//            .build();
//
//    UserDto updateUserDto = UserDto.builder()
//            .email("update@test.ru")
//            .name("updateName")
//            .build();
//
//    ItemDto itemDto = ItemDto.builder()
//            .name("itemName")
//            .description("itemDescription")
//            .available(true)
//            .build();
//
//    ItemDto itemDtoUpdate = ItemDto.builder()
//            .name("itemName")
//            .description("itemDescription")
//            .available(false)
//            .build();
//
//    CommentDto commentDto = CommentDto.builder()
//            .text("comment text")
//            .authorName(userDto.getName())
//            .build();
//
//    BookingRequestDto bookingRequestDtoDto = BookingRequestDto.builder()
//            .start(LocalDateTime.of(2025, 01, 01, 01, 01))
//            .end(LocalDateTime.of(2025, 02, 01, 01, 01))
//            .itemId(1L)
//            .build();
//
//    @Test
//    void testCreateUser() {
//        userController.createUser(userDto);
//        assertEquals(1, userController.getAllUsers().size());
//    }
//
//    @Test
//    void testGetUser() {
//        userController.createUser(userDto);
//        assertEquals("testName", userController.getUser(1).getName());
//        assertEquals("test@test.ru", userController.getUser(1).getEmail());
//    }
//
//    @Test
//    void testUpdateUser() {
//        userController.createUser(userDto);
//        userController.updateUser(1, updateUserDto);
//        assertEquals("updateName", userController.getUser(1).getName());
//    }
//
//    @Test
//    void testGetAllUsers() {
//        userController.createUser(userDto);
//        userController.createUser(updateUserDto);
//        assertEquals(2, userController.getAllUsers().size());
//    }
//
//    @Test
//    void testDeleteUser() {
//        userController.createUser(userDto);
//        userController.createUser(updateUserDto);
//        userController.deleteUser(1);
//        assertEquals(1, userController.getAllUsers().size());
//    }
//
//    @Test
//    void testAddItem() {
//        userController.createUser(userDto);
//        itemController.addItem(1L, itemDto);
//        assertEquals("itemName", itemController.getItem(1L, 1).getName());
//        assertEquals(1, itemController.getItems(1).size());
//    }
//
//    @Test
//    void testUpdateItem() {
//        userController.createUser(userDto);
//        itemController.addItem(1L, itemDto);
//        itemController.update(1L, 1L, itemDtoUpdate);
//        assertEquals(false, itemController.getItem(1L, 1L).getAvailable());
//    }
//
//    @Test
//    void testSearchItem() {
//        userController.createUser(userDto);
//        itemController.addItem(1L, itemDto);
//        assertEquals(1, itemController.searchItem("description").size());
//    }
//
//    @Test
//    void testDeleteItem() {
//        userController.createUser(userDto);
//        itemController.addItem(1L, itemDto);
//        itemController.addItem(1L, itemDtoUpdate);
//        itemController.deleteItem(1L, 2L);
//        assertEquals(1, itemController.getItems(1L).size());
//    }
//
//    @Test
//    void addBooking() {
//        userController.createUser(userDto);
//        userController.createUser(updateUserDto);
//        itemController.addItem(1L, itemDto);
//        bookingController.addBooking(2L, bookingRequestDtoDto);
//        assertEquals(1, bookingController.getBooking(1, 1).getId());
//        assertEquals(2, bookingController.getBooking(1, 1).getBooker().getId());
//        assertEquals(Status.WAITING, bookingController.getBooking(1, 1).getStatus());
//    }
//
//    @Test
//    void getBooking() {
//        userController.createUser(userDto);
//        userController.createUser(updateUserDto);
//        itemController.addItem(1L, itemDto);
//        bookingController.addBooking(2L, bookingRequestDtoDto);
//        assertEquals(1, bookingController.getBooking(1l,2l));
//    }
}
