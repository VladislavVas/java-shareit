package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public static Item toItem(ItemDto dto) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .build();
    }

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static List<Item> toListItem(List<ItemDto> dtoList) {
        return dtoList.stream()
                .map(ItemMapper::toItem)
                .collect(Collectors.toList());

    }

    public static List<ItemDto> toDtoList(List<Item> itemList) {
        return itemList.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

}
