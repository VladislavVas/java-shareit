package ru.practicum.shareit.booking.bookingService;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import java.util.List;

public interface BookingService {

    BookingResponseDto addBooking(BookingRequestDto bookingRequestDto, long userId);

    BookingResponseDto approveBooking(long bookingId, Boolean approved, long userId);

    List<BookingResponseDto> getSortedListBookingByUserId(long userId, State state);

    BookingResponseDto getBookingByUserId(long userId, long bookingId);

    List<BookingResponseDto> getListBookingByOwnerId(long userId, State state);
}
