package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookingStorageTest {

    @Autowired
    private BookingStorage bookingStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private ItemStorage itemStorage;
    private User owner;
    private User booker;
    private Booking booking;
    private Item item;
    private final LocalDateTime start = LocalDateTime.of(2025, 01, 01, 01, 01);
    private final LocalDateTime end = LocalDateTime.of(2025, 02, 01, 01, 01);

    @BeforeEach
    void create() {
        owner = new User("owner", "owner@gmail.com");
        booker = new User("booker", "boker@gmail.com");
        item = new Item("item", "item description", true, owner, null);
        booking = new Booking(start, end, item, booker, Status.WAITING);
        owner = userStorage.save(owner);
        booker = userStorage.save(booker);
        item = itemStorage.save(item);
    }

    @Test
    void findBookingByBookerId() {
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findBookingByBookerId(booker.getId(), PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void findBookingsCurrentForBooker() {
        booking.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2030, 01, 01, 01, 01));
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findBookingsCurrentForBooker(booker.getId(), LocalDateTime.now(),
                PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void findBookingsPastForBooker() {
        booking.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2001, 01, 01, 01, 01));
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findBookingsPastForBooker(booker.getId(), LocalDateTime.now(),
                PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void findBookingsFutureForBooker() {
        booking.setStart(LocalDateTime.of(2030, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2035, 01, 01, 01, 01));
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findBookingsFutureForBooker(booker.getId(), LocalDateTime.now(),
                PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void findBookingsByStatusAndBookerId() {
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findBookingsByStatusAndBookerId(booker.getId(), Status.WAITING,
                PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void findAllBookingsForItemOwner() {
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findAllBookingsForItemOwner(owner.getId(),
                PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void findBookingsCurrentForItemOwner() {
        booking.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2030, 01, 01, 01, 01));
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findBookingsCurrentForItemOwner(owner.getId(), LocalDateTime.now(),
                PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void findBookingsPastForItemOwner() {
        booking.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2001, 01, 01, 01, 01));
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findBookingsPastForItemOwner(owner.getId(), LocalDateTime.now(),
                PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void findBookingsFutureForItemOwner() {
        booking.setStart(LocalDateTime.of(2030, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2035, 01, 01, 01, 01));
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findBookingsFutureForItemOwner(owner.getId(), LocalDateTime.now(),
                PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void findBookingsByStatusForItemOwner() {
        booking = bookingStorage.save(booking);
        List<Booking> bookings = bookingStorage.findBookingsByStatusForItemOwner(owner.getId(), Status.WAITING,
                PageRequest.of(0, 2));
        assertThat(bookings).hasSize(1).contains(booking);
    }

    @Test
    void isFree() {
        booking.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2001, 01, 01, 01, 01));
        bookingStorage.save(booking);
        boolean isBooked = bookingStorage.isFree(LocalDateTime.now(),
                LocalDateTime.now(),
                item.getId());
        assertTrue(isBooked);
    }

    @Test
    void findBookingByItemWithDateBefore() {
        booking.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2030, 01, 01, 01, 01));
        booking = bookingStorage.save(booking);

        assertEquals(booking, bookingStorage.findBookingByItemWithDateBefore(item.getId(), LocalDateTime.now()));
    }

    @Test
    void findBookingByItemWithDateAfter() {
        booking.setStart(LocalDateTime.of(2030, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2035, 01, 01, 01, 01));
        booking = bookingStorage.save(booking);
        assertEquals(booking, bookingStorage.findBookingByItemWithDateAfter(item.getId(), LocalDateTime.now()));
    }

    @Test
    void isExists() {
        booking.setStart(LocalDateTime.of(2000, 01, 01, 01, 01));
        booking.setEnd(LocalDateTime.of(2001, 01, 01, 01, 01));
        bookingStorage.save(booking);
        boolean isExist = bookingStorage.isExists(item.getId(), booker.getId(), LocalDateTime.now());
        assertTrue(isExist);
    }
}