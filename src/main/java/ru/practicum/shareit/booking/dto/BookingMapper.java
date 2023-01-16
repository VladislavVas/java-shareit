package ru.practicum.shareit.booking.dto;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class BookingMapper {

    public static Booking toBooking(BookingRequestDto bookingRequestDto, Item item, User booker) {
        return Booking.builder()
                .start(bookingRequestDto.getStart())
                .end(bookingRequestDto.getEnd())
                .item(item)
                .booker(booker).build();

    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(new BookingResponseDto.Booker(booking.getBooker().getId(), booking.getBooker().getName()))
                .item(new BookingResponseDto.ItemBooking(booking.getItem().getId(),booking.getItem().getName()))
                .status(booking.getStatus()).build();
    }

    public static List<BookingResponseDto> toListBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }
}
