package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingResponseDtoTest {
    @Autowired
    private JacksonTester<BookingResponseDto> json;
    private BookingResponseDto bookingResponseDto;

    @Test
    void setBookingResponseDtoTest() throws IOException {
        bookingResponseDto = BookingResponseDto.builder()
                .id(1L)
                .item(new BookingResponseDto.ItemBooking(1L, "item"))
                .start(LocalDateTime.of(2025, 01, 01, 01, 01))
                .end(LocalDateTime.of(2025, 02, 01, 01, 01))
                .booker(new BookingResponseDto.Booker(1L, "booker"))
                .build();

        JsonContent<BookingResponseDto> result = json.write(bookingResponseDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
        assertThat(result).extractingJsonPathValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("booker");
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo("2025-01-01T01:01:00");
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo("2025-02-01T01:01:00");
    }
}
