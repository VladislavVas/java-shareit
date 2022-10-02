package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Item {
    private long id;
    private String name;
    private String description;
    private String available;
    private long ownerId;
}
