package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.Create;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(value = "from", required = false, defaultValue = "0")
                                           int from,
                                           @RequestParam(value = "size", required = false, defaultValue = "20")
                                           int size) {
        log.info("ItemController: GET-запрос всех вещей пользователя id " + userId);
        return itemClient.getAllItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long itemId) {
        log.info("ItemController: GET-запрос вещи id " + itemId + " пользователя id " + userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text,
                                             @RequestParam(value = "from", required = false, defaultValue = "0")
                                             int from,
                                             @RequestParam(value = "size", required = false, defaultValue = "20")
                                             int size) {
        log.info("ItemController: GET-запрос вещи по описанию: " + text);
        return itemClient.searchItem(text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        log.info("ItemController: POST-запрос на добавление вещи пользователю id " + userId);
        return itemClient.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable long itemId,
                                         @RequestBody ItemDto itemDto) {
        log.info("ItemController: PATCH-запрос на обновление вещи id " + itemId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@Validated(Create.class) @RequestBody CommentDto commentDto,
                                                   @PathVariable long itemId,
                                                   @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemClient.addCommentToItem(commentDto, itemId, userId);
    }
}
