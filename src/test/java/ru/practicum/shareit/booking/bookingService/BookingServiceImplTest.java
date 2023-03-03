package ru.practicum.shareit.booking.bookingService;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.itemService.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplTest {

    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private UserServiceImpl userService;
    private BookingResponseDto bookingResponseDto;
    private BookingRequestDto bookingRequestDto;
    private UserDto userDto;
    private UserDto userDto2;
    private ItemDto itemDto;
    private User user1;
    private User user2;
    private Item item;
    private List<BookingResponseDto> bookingsList;

    @BeforeEach
    void getEntities() {
        bookingRequestDto = BookingRequestDto.builder()
                .start(LocalDateTime.of(2025, 01, 01, 01, 01))
                .end(LocalDateTime.of(2025, 02, 01, 01, 01))
                .itemId(1L)
                .build();
        userDto = UserDto.builder()
                .email("test@test.ru")
                .name("testName")
                .build();
        userDto2 = UserDto.builder()
                .email("booker@test.ru")
                .name("bookerName")
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("itemDto_1_name")
                .description("itemDto_1_description")
                .available(true)
                .requestId(null)
                .build();
        user1 = UserMapper.toUser(userService.addUser(userDto));
        user2 = UserMapper.toUser(userService.addUser(userDto2));
        item = ItemMapper.toItem(itemService.addNewItem(1L, itemDto), user1, null);
    }

    @Test
    void addBooking() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        assertThat(bookingResponseDto.getId()).isNotZero();
        assertThat(bookingResponseDto.getBooker().getId()).isEqualTo(2L);
        assertThat(bookingResponseDto.getBooker().getName()).isEqualTo("bookerName");
        assertThat(bookingResponseDto.getStart()).isEqualTo("2025-01-01T01:01:00");
        assertThat(bookingResponseDto.getEnd()).isEqualTo("2025-02-01T01:01:00");
        assertThat(bookingResponseDto.getItem().getId()).isEqualTo(item.getId());
        assertThat(bookingResponseDto.getItem().getName()).isEqualTo(item.getName());
        assertThat(bookingResponseDto.getStatus()).isEqualTo(Status.WAITING);

    }

    @Test
    void approveBooking() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingResponseDto.setStatus(Status.APPROVED);
        assertEquals(bookingResponseDto, bookingService.approveBooking(bookingResponseDto.getId(),
                true, user1.getId()));
    }

    @Test
    void getSortedListBookingByUserId() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getSortedListBookingByUserId(user2.getId(),
                State.ALL, 0, 20));
    }

    @Test
    void getBookingByUserId() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        assertEquals(bookingResponseDto, bookingService.getBookingByUserId(bookingResponseDto.getId(), user1.getId()));
    }

    @Test
    void getListBookingByOwnerId() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getListBookingByOwnerId(user1.getId(),
                State.ALL, 0, 20));
    }

    @Test
    void getListBookingByOwnerIdWithException() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        ValidateException e = assertThrows(ValidateException.class,
                () -> bookingService.getListBookingByOwnerId(user1.getId(),
                        State.UNSUPPORTED_STATUS, 0, 20));
        assertThat(e.getMessage()).contains(
                String.format("Unknown state: UNSUPPORTED_STATUS"));
    }

    @Test
    void getListBookingByUserIdWithException() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        ValidateException e = assertThrows(ValidateException.class,
                () -> bookingService.getSortedListBookingByUserId(user2.getId(),
                        State.UNSUPPORTED_STATUS, 0, 20));
        assertThat(e.getMessage()).contains(
                String.format("Unknown state: UNSUPPORTED_STATUS"));
    }

    @Test
    void getSortedListBookingByUserIdCurrentState() {
        bookingRequestDto.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        bookingRequestDto.setEnd(LocalDateTime.of(2030, 01, 01, 01, 01));
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getSortedListBookingByUserId(2L, State.CURRENT, 0, 20));
    }

    @Test
    void getSortedListBookingByUserIdPastState() {
        bookingRequestDto.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        bookingRequestDto.setEnd(LocalDateTime.of(2001, 01, 01, 01, 01));
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getSortedListBookingByUserId(2L, State.PAST, 0, 20));
    }

    @Test
    void getSortedListBookingByUserIdFutureState() {
        bookingRequestDto.setStart(LocalDateTime.of(2029, 01, 01, 01, 01));
        bookingRequestDto.setEnd(LocalDateTime.of(2030, 01, 01, 01, 01));
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getSortedListBookingByUserId(2L, State.FUTURE, 0, 20));
    }

    @Test
    void getSortedListBookingByUserIdWaitingState() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getSortedListBookingByUserId(2L, State.WAITING, 0, 20));
    }

    @Test
    void getSortedListBookingByOwnerIdWaitingState() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getListBookingByOwnerId(1L, State.WAITING, 0, 20));
    }

    @Test
    void getSortedListBookingByOwnerIdRejectedState() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingResponseDto = bookingService.approveBooking(1L, false, 1L);
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getListBookingByOwnerId(1L, State.REJECTED, 0, 20));
    }

    @Test
    void getSortedListBookingByUserIdRejectedState() {
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingResponseDto = bookingService.approveBooking(1L, false, 1L);
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getSortedListBookingByUserId(2L, State.REJECTED, 0, 20));
    }

    @Test
    void getSortedListBookingByOwnerIdFutureState() {
        bookingRequestDto.setStart(LocalDateTime.of(2029, 01, 01, 01, 01));
        bookingRequestDto.setEnd(LocalDateTime.of(2030, 01, 01, 01, 01));
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getListBookingByOwnerId(1L, State.FUTURE, 0, 20));
    }

    @Test
    void getSortedListBookingByOwnerIdPastState() {
        bookingRequestDto.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        bookingRequestDto.setEnd(LocalDateTime.of(2001, 01, 01, 01, 01));
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getListBookingByOwnerId(1L, State.PAST, 0, 20));
    }

    @Test
    void getSortedListBookingByOwnerIdCurrentState() {
        bookingRequestDto.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        bookingRequestDto.setEnd(LocalDateTime.of(2030, 01, 01, 01, 01));
        bookingResponseDto = bookingService.addBooking(bookingRequestDto, user2.getId());
        bookingsList = List.of(bookingResponseDto);
        assertEquals(bookingsList, bookingService.getListBookingByOwnerId(1L, State.CURRENT, 0, 20));
    }

    @Test
    void testWrongPage() {
        ValidateException e = assertThrows(ValidateException.class,
                () -> bookingService.getListBookingByOwnerId(user1.getId(),
                        State.ALL, -1, -1));
        assertThat(e.getMessage()).contains(
                String.format("Неверные параметры page или size"));
    }

}