package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoForRequestTest {
    @Autowired
    private JacksonTester<ItemDtoForRequest> json;
    private ItemDtoForRequest itemDtoForRequest;

    @Test
    void itemDtoForRequestTest() throws IOException {
        itemDtoForRequest = ItemDtoForRequest.builder()
                .id(1L)
                .name("item name")
                .description("description")
                .lastBooking(new ItemDtoForRequest.BookingDto(
                        1L,
                        LocalDateTime.of(2025, 01, 01, 01, 01),
                        LocalDateTime.of(2025, 02, 01, 01, 01),
                        1L))
                .nextBooking(new ItemDtoForRequest.BookingDto(
                        2L,
                        LocalDateTime.of(2026, 01, 01, 01, 01),
                        LocalDateTime.of(2026, 02, 01, 01, 01),
                        1L))
                .available(false)
                .comments(Collections.emptyList())
                .requestId(1L)
                .build();

        JsonContent<ItemDtoForRequest> result = json.write(itemDtoForRequest);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.requestId");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isFalse();
        assertThat(result).extractingJsonPathValue("$.requestId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.nextBooking.id").isEqualTo(2);
        assertThat(result).extractingJsonPathValue("$.requestId").isEqualTo(1);
    }
}
