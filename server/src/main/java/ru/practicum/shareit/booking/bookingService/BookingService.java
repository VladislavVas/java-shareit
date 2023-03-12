package ru.practicum.shareit.booking.bookingService;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto addBooking(BookingRequestDto bookingRequestDto, long userId);

    BookingResponseDto approveBooking(long bookingId, Boolean approved, long userId);

    List<BookingResponseDto> getSortedListBookingByUserId(long userId, State state, int from, int size);

    BookingResponseDto getBookingByUserId(long userId, long bookingId);

    List<BookingResponseDto> getListBookingByOwnerId(long userId, State state, int from, int size);
}
