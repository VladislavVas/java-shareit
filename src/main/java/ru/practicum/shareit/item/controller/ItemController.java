package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.itemService.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemController: GET-запрос всех вещей пользователя id " + userId);
        return itemServiceImpl.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        log.info("ItemController: GET-запрос вещи id " + itemId + " пользователя id " + userId);
        return itemServiceImpl.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.info("ItemController: GET-запрос вещи по описанию: " + text);
        return itemServiceImpl.searchItemByText(text);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("ItemController: POST-запрос на добавление вещи пользователю id " + userId);
        return itemServiceImpl.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("ItemController: PATCH-запрос на обновление вещи id " + itemId);
        return itemServiceImpl.updateItem(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        log.info("ItemController: DELETE-запрос на удаление вещи id " + itemId + "у пользователя " + userId);
        itemServiceImpl.deleteItem(userId, itemId);
    }
}
