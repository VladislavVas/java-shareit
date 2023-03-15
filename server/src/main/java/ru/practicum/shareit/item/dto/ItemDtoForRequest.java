package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDtoForRequest {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;

    @Data
    public static class BookingDto {
        private final Long id;
        private final LocalDateTime start;
        private final LocalDateTime end;
        private final Long bookerId;
    }
}
