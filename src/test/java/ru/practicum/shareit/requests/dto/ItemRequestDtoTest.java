package ru.practicum.shareit.requests.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;
    private ItemRequestDto itemRequestDto;

    @Test
    void setItemRequestDtoTest() throws IOException {
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .items(Collections.emptyList())
                .requesterId(1L)
                .description("description")
                .build();

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.items");
        assertThat(result).hasJsonPath("$.requesterId");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.requesterId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
    }

}