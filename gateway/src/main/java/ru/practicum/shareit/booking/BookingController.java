package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid  BookItemRequestDto bookingRequestDto) {
        log.info("BookingController: POST запрос на бронирование");
        return bookingClient.addBooking(bookingRequestDto, userId);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable long bookingId,
                                                 @RequestParam Boolean approved,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("BookingController: PATH запрос на подтверждение бронирования");
        return bookingClient.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("BookingController: GET запрос на бронирования пользователя " + userId);
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getListBookingByOwnerId(
            @RequestParam(name = "state", required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(value = "from", required = false, defaultValue = "0") int from,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        return bookingClient.getListBookingByOwnerId(ownerId, state, from, size);
    }

    @GetMapping()
    public ResponseEntity<Object> getSortedListBookingByUserId(
            @RequestParam(name = "state", required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") long bookerId,
            @RequestParam(value = "from", required = false, defaultValue = "0") int from,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        return bookingClient.getSortedListBookingByUserId(bookerId, state, from, size);
    }
}
