package ru.practicum.shareit.item.itemService;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import java.util.List;

public interface ItemService {

    List<ItemDtoForRequest> getAllItems(long userId);

    ItemDtoForRequest getItem(long userId, long itemId);

    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    void deleteItem(long userId, long itemId);

    List<ItemDto> searchItemByText(String text);
}

