package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

    @Repository
    public class ItemStorageInMemory {
        private long itemId;
        private final Map<Long, List<ItemDto>> userItems = new HashMap<>();

        public List<ItemDto> getItems(long userId) {
            return userItems.get(userId);
        }

        public ItemDto getItemById(long itemId) {
            return userItems.values().stream()
                    .flatMap(list -> list.stream())
                    .filter(item -> item.getId() == itemId)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена",
                            itemId)));
        }

        public ItemDto add(long userId, ItemDto itemDto) {
            if (!userItems.containsKey(userId)) {
                userItems.put(userId, new ArrayList<>());
            }
            itemDto.setId(generateId());
            userItems.get(userId).add(itemDto);

            return itemDto;
        }

        public ItemDto update(long userId, ItemDto itemDtoExisting, ItemDto itemDto) {
            if (!(itemDto.getAvailable() == null)) {
                itemDtoExisting.setAvailable(itemDto.getAvailable());
            }
            if (!(itemDto.getName() == null)) {
                itemDtoExisting.setName(itemDto.getName());
            }
            if (!(itemDto.getDescription() == null)) {
                itemDtoExisting.setDescription(itemDto.getDescription());
            }
            return itemDtoExisting;
        }

        public void delete(long userId, long itemId) {
            for (ItemDto itemOfUser : userItems.get(userId)) {
                if (itemOfUser.getId() == itemId) {
                    userItems.get(userId).remove(itemOfUser);
                }
            }
        }

        public List<ItemDto> search(String text) {
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

        public ItemDto getItemByIdAndUserId(long userId, long itemId) {
            if (!userItems.containsKey(userId)) {
                throw new NotFoundException(String.format("У пользователя с id %d нет вещей",
                        userId));
            }

            return userItems.get(userId).stream()
                    .filter(p -> p.getId() == (itemId))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена",
                            itemId)));
        }
        private long generateId() {
            return itemId++;
        }
    }


}
