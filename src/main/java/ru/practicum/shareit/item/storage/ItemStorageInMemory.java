package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemStorageInMemory {
    private long itemId;
    private final Map<Long, List<Item>> userItems = new HashMap<>();

    public List<Item> getItems(long userId) {
        log.info("ItemStorage: выгрузка списка вещей пользователя id " + userId);
        return userItems.get(userId);
    }

    public Item getItem(long itemId) {
        log.info("ItemStorage: выгрузка вещи id " + itemId);
        Item item = userItems.values().stream()
                .flatMap(list -> list.stream())
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Вещь id " + itemId + "не найдена")));
        return item;
    }

    public Item addItem(long userId, Item item) {
        if (!userItems.containsKey(userId)) {
            userItems.put(userId, new ArrayList<>());
        }
        item.setId(generateId());
        userItems.get(userId).add(item);
        item.setOwnerId(userId);
        log.info("ItemStorage: добавление пользователю id " + userId + " вещи id " + item.getId());
        return item;
    }

    public Item updateItem(long userId, Item itemInMemory, Item item) {
        if (!userItems.containsKey(userId)) {
            throw new NotFoundException("Не найдены вещи пользователя с id " + userId);
        }
        if (item.getName() != null) {
            itemInMemory.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemInMemory.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemInMemory.setAvailable(item.getAvailable());
        }
        log.info("ItemStorage: у пользователя id " + userId + " обновлена вещь id " + itemInMemory.getId());
        return itemInMemory;
    }

    public void deleteItem(long userId, long itemId) {
        List<Item> userItemsList = userItems.get(userId);
        Iterator<Item> iterator = userItemsList.iterator();
        while (iterator.hasNext()) {
            Item userItem = iterator.next();
            if (itemId == userItem.getId()) {
                iterator.remove();
                log.info("ItemStorage: у пользователя id " + userId + " удалена вещь id " + itemId);
            }
        }
    }

    public List<Item> searchItem(String text) {
        List<Item> foundItems = new ArrayList<>();
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
