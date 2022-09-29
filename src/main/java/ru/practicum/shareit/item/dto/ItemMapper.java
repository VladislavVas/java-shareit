package ru.practicum.shareit.item.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring", uses = ItemMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ItemMapper {
    Item toItem(ItemDto dto);

    ItemDto toDto(Item item);

    List<Item> toListItem (List<ItemDto> dtoList);

    List<ItemDto> toDtoList (List<Item> itemList);
}
