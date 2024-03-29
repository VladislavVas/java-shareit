package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.bookingService.BookingServiceImpl;
import ru.practicum.shareit.booking.bookingService.State;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody BookItemRequestDto bookItemRequestDto) {
        log.info("BookingController: POST запрос на бронирование");
        return bookingService.addBooking(bookItemRequestDto, userId);
    }

    @PatchMapping("{bookingId}")
    public BookingResponseDto approveBooking(@PathVariable long bookingId,
                                             @RequestParam Boolean approved,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("BookingController: PATH запрос на подтверждение бронирования");
        return bookingService.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("{bookingId}")
    public BookingResponseDto getBooking(@PathVariable long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("BookingController: GET запрос на бронирования пользователя " + userId);
        return bookingService.getBookingByUserId(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getListBookingByOwnerId(
            @RequestParam(name = "state", required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(value = "from", required = false, defaultValue = "0") int from,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        return bookingService.getListBookingByOwnerId(ownerId, state, from, size);
    }

    @GetMapping()
    public List<BookingResponseDto> getSortedListBookingByUserId(
            @RequestParam(name = "state", required = false, defaultValue = "ALL") State state,
            @RequestHeader("X-Sharer-User-Id") long bookerId,
            @RequestParam(value = "from", required = false, defaultValue = "0") int from,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        return bookingService.getSortedListBookingByUserId(bookerId, state, from, size);
    }

}
