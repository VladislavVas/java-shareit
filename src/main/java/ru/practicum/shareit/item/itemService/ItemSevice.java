package ru.practicum.shareit.item.itemService;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemSevice {

    List<ItemDto> getAllItems(long userId);

    ItemDto getItem(long userId, long itemId);

    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    void deleteItem(long userId, long itemId);

    List<ItemDto> searchItemByText(String text);
}
