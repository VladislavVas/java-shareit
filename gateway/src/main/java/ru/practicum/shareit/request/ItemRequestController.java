package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.Create;

import ru.practicum.shareit.request.dto.ItemRequestDto;


@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") long requesterId,
                                             @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("ItemRequestController: POST на добавление нового запроса");
        return itemRequestClient.addRequest(itemRequestDto, requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestParam(value = "from", required = false, defaultValue = "0")
                                                 int from,
                                                 @RequestParam(value = "size", required = false, defaultValue = "20")
                                                 int size,
                                                 @RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("ItemRequestController: GET всех запросов");
        return itemRequestClient.getAllRequests(requesterId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsForRequester(@RequestHeader("X-Sharer-User-Id") long requesterId) {
        log.info("ItemRequestController: GET всех запросов для пользователя id=" + requesterId);
        return itemRequestClient.getAllRequestsForRequester(requesterId);
    }


    @GetMapping("{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("ItemRequestController: GET запроса id=" + requestId);
        return itemRequestClient.getRequest(requestId, userId);
    }
}
