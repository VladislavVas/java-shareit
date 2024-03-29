package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {

    private long id;
    private ItemBooking item;
    private Booker booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;

    @Data
    public static class Booker {
        private final long id;
        private final String name;
    }

    @Data
    public static class ItemBooking {
        private final long id;
        private final String name;
    }
}
