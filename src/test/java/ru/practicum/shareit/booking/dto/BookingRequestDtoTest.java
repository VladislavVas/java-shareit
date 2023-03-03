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
class BookingRequestDtoTest {
    @Autowired
    private JacksonTester<BookingRequestDto> json;
    private BookingRequestDto bookingRequestDto;

    @Test
    void bookingRequestDtoTest() throws IOException {
        bookingRequestDto = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2025, 01, 01, 01, 01))
                .end(LocalDateTime.of(2025, 02, 01, 01, 01))
                .build();

        JsonContent<BookingRequestDto> result = json.write(bookingRequestDto);
        assertThat(result).hasJsonPath("$.itemId");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).extractingJsonPathValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo("2025-01-01T01:01:00");
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo("2025-02-01T01:01:00");
    }
}