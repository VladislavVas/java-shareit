package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.itemService.ItemServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;

    @GetMapping
    public List<ItemDtoForRequest> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(value = "from", required = false, defaultValue = "0")
                                            int from,
                                            @RequestParam(value = "size", required = false, defaultValue = "20")
                                            int size) {
        log.info("ItemController: GET-запрос всех вещей пользователя id " + userId);
        return itemServiceImpl.getAllItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemDtoForRequest getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long itemId) {
        log.info("ItemController: GET-запрос вещи id " + itemId + " пользователя id " + userId);
        return itemServiceImpl.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text,
                                    @RequestParam(value = "from", required = false, defaultValue = "0")
                                    int from,
                                    @RequestParam(value = "size", required = false, defaultValue = "20")
                                    int size) {
        log.info("ItemController: GET-запрос вещи по описанию: " + text);
        return itemServiceImpl.searchItemByText(text, from, size);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody ItemDto itemDto) {
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


    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@RequestBody CommentDto commentDto,
                                       @PathVariable long itemId,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemServiceImpl.addCommentToItem(commentDto, itemId, userId);
    }
}
