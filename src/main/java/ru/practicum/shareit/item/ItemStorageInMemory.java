package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemStorageInMemory {
    private long itemId;
    private final Map<Long, List<ItemDto>> userItems = new HashMap<>();

    public List<ItemDto> getItems(long userId) {
        log.info("ItemStorage: выгрузка списка вещей пользователя id " + userId);
        return userItems.get(userId);
    }

    public ItemDto getItem(long itemId) {
        log.info("ItemStorage: выгрузка вещи id " + itemId);
        ItemDto itemDto = userItems.values().stream()
                .flatMap(list -> list.stream())
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь id " + itemId + "не найдена")));
        return itemDto;
    }

    public ItemDto addItem(long userId, ItemDto itemDto) {
        if (!userItems.containsKey(userId)) {
            userItems.put(userId, new ArrayList<>());
        }
        itemDto.setId(generateId());
        userItems.get(userId).add(itemDto);
        itemDto.setOwnerId(userId);
        log.info("ItemStorage: добавление пользователю id " + userId + " вещи id " + itemDto.getId());
        return itemDto;
    }

    public ItemDto updateItem(long userId, ItemDto itemInMemory, ItemDto itemDto) {
        if (!userItems.containsKey(userId)) {
            throw new NotFoundException("Не найдены вещи пользователя с id " + userId);
        }
        if (itemDto.getName() != null) {
            itemInMemory.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemInMemory.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemInMemory.setAvailable(itemDto.getAvailable());
        }
        log.info("ItemStorage: у пользователя id " + userId + " обновлена вещь id " + itemInMemory.getId());
        return itemInMemory;
    }

    public void deleteItem(long userId, long itemId) {
        List<ItemDto> userItemsList = userItems.get(userId);
        Iterator<ItemDto> iterator = userItemsList.iterator();
        while (iterator.hasNext()) {
            ItemDto userItem = iterator.next();
            if (itemId == userItem.getId()) {
                iterator.remove();
                log.info("ItemStorage: у пользователя id " + userId + " удалена вещь id " + itemId);
            }
        }
    }

    public List<ItemDto> searchItem(String text) {
        List<ItemDto> foundItems = new ArrayList<>();
        if (!text.isBlank()) {
            foundItems = userItems.values().stream()
                    .flatMap(list -> list.stream())
                    .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase())
                            && item.getAvailable().equals("true"))
                    .collect(Collectors.toList());
        }
        return foundItems;
    }

    private long generateId() {
        return ++itemId;
    }
}
