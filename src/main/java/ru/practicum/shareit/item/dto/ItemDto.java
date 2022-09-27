package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.AvailableSerializer;

import javax.validation.constraints.NotBlank;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
@Builder
public class ItemDto {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    @JsonSerialize(using = AvailableSerializer.class)
    private String available;
    private long ownerId;
}
